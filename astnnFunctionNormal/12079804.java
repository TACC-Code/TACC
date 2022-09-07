class BackupThread extends Thread {
    public static byte[] getFileContent(String fileLocation) throws FileNotFoundException, IOException {
        ByteArrayOutputStream baos = null;
        InputStream is = new FileInputStream(fileLocation);
        baos = new ByteArrayOutputStream();
        while (is.available() > 0) {
            baos.write(is.read());
        }
        return baos.toByteArray();
    }
}
