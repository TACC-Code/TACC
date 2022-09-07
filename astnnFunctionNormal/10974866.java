class BackupThread extends Thread {
    protected boolean copy(InstallMonitor moni) throws AbortInstallException {
        boolean retryCopy = true;
        String fullName = Info.getSystemActions().getTargetName(location, target);
        moni.showCopyOp(source, fullName);
        if (!backupFile(moni, fullName)) return false;
        while (retryCopy) {
            MessageDigest sha = null;
            try {
                sha = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException e) {
                System.err.println("can get instance of an SHA-1 Digest Object");
                sha = null;
            }
            InputStream is = null;
            boolean retry = true;
            while (retry) {
                try {
                    is = Info.getInstallationSource().getFile(source, location);
                    retry = false;
                } catch (IOException e) {
                    retry = moni.showIOException(e);
                    if (retry == false) {
                        throw new AbortInstallException();
                    }
                }
            }
            if (is == null) return false;
            OutputStream os = Info.getSystemActions().openOutputFile(location, target);
            if (os == null) return false;
            byte[] buffer = new byte[4096];
            try {
                int bytes = 0;
                try {
                    while ((bytes = is.read(buffer)) >= 0) {
                        if (sha != null) sha.update(buffer, 0, bytes);
                        os.write(buffer, 0, bytes);
                        moni.addInstalledBytes(bytes);
                    }
                } catch (EOFException e) {
                }
                os.flush();
                os.close();
                is.close();
                if (sha != null) {
                    if ((digest == null) || (digest.length == 0)) {
                        digest = sha.digest();
                        retryCopy = false;
                    } else {
                        byte[] newDigest = sha.digest();
                        if (!sha.isEqual(digest, newDigest)) {
                            System.err.println("Checksum error for file " + fullName);
                            retryCopy = moni.showChecksumError(fullName);
                        } else {
                            retryCopy = false;
                        }
                        instdigest = newDigest;
                    }
                }
            } catch (IOException e) {
                System.err.println("error while copying " + source + " to " + fullName + ": " + e);
                retryCopy = moni.showIOException(e);
            }
        }
        installedName = fullName;
        wasInstalled = true;
        File instf = new File(fullName);
        lastModified = instf.lastModified();
        size = instf.length();
        return true;
    }
}
