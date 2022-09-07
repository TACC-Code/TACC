class BackupThread extends Thread {
    public static byte[] calcHash(File f) {
        FileInputStream is = null;
        byte hash[] = null;
        try {
            is = new FileInputStream(f);
            byte readBuffer[] = new byte[4096];
            MessageDigest md = MessageDigest.getInstance("MD5");
            int bytesRead = -1;
            while ((bytesRead = is.read(readBuffer)) > 0) {
                md.update(readBuffer, 0, bytesRead);
            }
            hash = md.digest();
        } catch (NoSuchAlgorithmException ex) {
            log.error("MD5 algorithm not found");
        } catch (FileNotFoundException ex) {
            log.error(f.getAbsolutePath() + "not found");
        } catch (IOException ex) {
            log.error("IOException while calculating hash: " + ex.getMessage());
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ex) {
                log.error("Cannot close stream after calculating hash");
            }
        }
        return hash;
    }
}
