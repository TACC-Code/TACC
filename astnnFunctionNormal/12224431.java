class BackupThread extends Thread {
    public byte[] getFileDataFromNode(String name, Node node) throws IOException, MalformedDirectoryItemException {
        InputStream is = getInputStreamFromNode(name, node);
        byte[] buffer = new byte[4096];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int read = 0;
        while ((read = is.read(buffer)) > 0) {
            baos.write(buffer, 0, read);
        }
        return baos.toByteArray();
    }
}
