class BackupThread extends Thread {
    public byte[] getRawParameter(String name) throws IOException {
        if (name == null) {
            return null;
        }
        File tempFile = (File) this.tempFileNames.get(name);
        if (tempFile == null) {
            return null;
        }
        InputStream inFile = new FileInputStream(tempFile);
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        byte buffer[] = new byte[2048];
        int readBytes = inFile.read(buffer);
        while (readBytes != -1) {
            byteArray.write(buffer, 0, readBytes);
            readBytes = inFile.read(buffer);
        }
        inFile.close();
        byte output[] = byteArray.toByteArray();
        byteArray.close();
        return output;
    }
}
