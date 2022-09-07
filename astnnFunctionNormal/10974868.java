class BackupThread extends Thread {
    public boolean wasModified() {
        File instf = new File(installedName);
        if (!instf.exists()) {
            System.err.println("File " + installedName + " does not exist !");
            return true;
        }
        if ((size != 0) && (instf.length() != size)) {
            System.err.println("stored size = " + size + " actual size " + instf.length());
            return true;
        }
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("can not get instance of an SHA-1 Digest Object");
            sha = null;
        }
        if ((sha == null) || (instdigest == null) || (instdigest.length == 0)) {
            if (instf.lastModified() != lastModified) {
                System.err.println("modification time differs");
                return true;
            }
            return false;
        }
        try {
            int bytes = 0;
            byte[] buffer = new byte[4096];
            FileInputStream is = new FileInputStream(instf);
            while ((bytes = is.read(buffer)) >= 0) {
                sha.update(buffer, 0, bytes);
            }
            is.close();
        } catch (IOException e) {
            System.err.println("error while reading file " + installedName);
            return true;
        }
        byte[] newDigest = sha.digest();
        if (!sha.isEqual(newDigest, instdigest)) {
            System.err.println("digest mismatch");
            return true;
        }
        return false;
    }
}
