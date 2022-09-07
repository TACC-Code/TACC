class BackupThread extends Thread {
    private byte[] getCertificate() throws IOException {
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            fis = new FileInputStream("/Users/openmobster/desktop/aps_dev.p12");
            bos = new ByteArrayOutputStream();
            int readByte;
            while ((readByte = fis.read()) != -1) {
                bos.write(readByte);
            }
            return bos.toByteArray();
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (bos != null) {
                bos.close();
            }
        }
    }
}
