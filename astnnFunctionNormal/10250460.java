class BackupThread extends Thread {
    protected boolean subst(InstallMonitor moni) throws AbortInstallException {
        String fullName = Info.getSystemActions().getTargetName(location, target);
        moni.showCopyOp(source, fullName);
        if (!backupFile(moni, fullName)) return false;
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
                is = Info.getInstallationSource().getFile(source, temploc);
                retry = false;
            } catch (IOException e) {
                retry = moni.showIOException(e);
                if (retry == false) {
                    throw new AbortInstallException();
                }
            }
        }
        if (is == null) {
            System.err.println("can not open template " + source + " for substitution");
            return false;
        }
        OutputStream os = Info.getSystemActions().openOutputFile(location, target);
        if (os == null) {
            System.err.println("can not open target target " + fullName + " for substitution");
            return false;
        }
        OutputStream ds = null;
        if (sha != null) {
            ds = new DigestOutputStream(os, sha);
            ((DigestOutputStream) ds).on(true);
        } else {
            ds = os;
        }
        SubstFilter subst = new SubstFilter(is);
        subst.setOutput(new PrintStream(ds));
        try {
            subst.scan();
            ds.flush();
            ds.close();
            is.close();
        } catch (IOException e) {
            System.err.println("error while copying from" + source + " to " + fullName + ": " + e);
            return false;
        }
        if (sha != null) {
            instdigest = sha.digest();
        }
        installedName = fullName;
        wasInstalled = true;
        File instf = new File(fullName);
        lastModified = instf.lastModified();
        size = instf.length();
        return true;
    }
}
