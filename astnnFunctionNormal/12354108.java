class BackupThread extends Thread {
    private byte[] writeFile(File f, DataHandler dh) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = null;
        OutputStream out = null;
        if (!f.exists()) {
            log.debug("#### Write File:" + f);
            try {
                f.getParentFile().mkdirs();
                FileOutputStream fos = new FileOutputStream(f);
                if (computeHash) {
                    md = MessageDigest.getInstance("SHA1");
                    out = new DigestOutputStream(fos, md);
                } else {
                    log.debug("SHA1 feature is disabled!");
                    out = fos;
                }
                dh.writeTo(out);
                log.debug("#### File written:" + f + " exists:" + f.exists());
            } finally {
                if (out != null) try {
                    out.close();
                } catch (IOException ignore) {
                    log.error("Ignored error during close!", ignore);
                }
            }
        }
        return md == null ? null : md.digest();
    }
}
