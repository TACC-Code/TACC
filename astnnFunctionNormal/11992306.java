class BackupThread extends Thread {
    @SuppressWarnings("empty-statement")
    public static String generateFileChecksum(String hash, String filename) {
        String checksum;
        FileInputStream fis = null;
        try {
            MessageDigest md = MessageDigest.getInstance(hash);
            fis = new FileInputStream(filename);
            byte[] dataBytes = new byte[1024];
            int nread = 0;
            while ((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            }
            ;
            byte[] mdbytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mdbytes.length; i++) {
                sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            checksum = sb.toString();
        } catch (IOException ex) {
            if (debug == true) Logger.getLogger(ScannerChecksum.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        } catch (NoSuchAlgorithmException ex) {
            if (debug == true) Logger.getLogger(ScannerChecksum.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        } finally {
            try {
                if (fis != null) fis.close();
            } catch (IOException ex) {
                if (debug == true) Logger.getLogger(ScannerChecksum.class.getName()).log(Level.SEVERE, null, ex);
                return "";
            }
        }
        return checksum;
    }
}
