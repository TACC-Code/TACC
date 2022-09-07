class BackupThread extends Thread {
    protected boolean copyText(InstallMonitor moni) throws AbortInstallException {
        String fullName = Info.getSystemActions().getTargetName(location, target);
        boolean retryCopy = true;
        if (!backupFile(moni, fullName)) return false;
        moni.showCopyOp(source, fullName);
        while (retryCopy) {
            MessageDigest insha = null;
            MessageDigest sha = null;
            try {
                sha = MessageDigest.getInstance("SHA-1");
                insha = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException e) {
                System.err.println("can get instance of an SHA-1 Digest Object");
                sha = null;
                insha = null;
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
            OutputStream ods = null;
            if (sha != null) {
                ods = new DigestOutputStream(os, sha);
                ((DigestOutputStream) ods).on(true);
            } else {
                ods = os;
            }
            InputStream ids = null;
            if (insha != null) {
                ids = new DigestInputStream(is, insha);
                ((DigestInputStream) ids).on(true);
            } else {
                ids = is;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(ids), 4096);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(ods));
            try {
                String data = null;
                while ((data = br.readLine()) != null) {
                    bw.write(data);
                    bw.newLine();
                    moni.addInstalledBytes(data.length() + 1);
                }
                bw.flush();
                bw.close();
                br.close();
                ids.close();
                retryCopy = false;
            } catch (IOException e) {
                System.err.println("error while copying " + source + " to " + fullName + ": " + e);
                retryCopy = moni.showIOException(e);
            }
            if (insha != null) {
                byte[] calcDigest = insha.digest();
                if (!insha.isEqual(digest, calcDigest)) {
                    System.err.println("Checksum error for file " + fullName);
                    retryCopy = moni.showChecksumError(fullName);
                }
            }
            if (sha != null) {
                instdigest = sha.digest();
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
