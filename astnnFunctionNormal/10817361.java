class BackupThread extends Thread {
    public void generateHash() throws IOException {
        final FileInputStream fileInputStream = new FileInputStream(_path);
        final FileChannel fileChannel = fileInputStream.getChannel();
        final ByteBuffer fileData = ByteBuffer.allocate((int) fileChannel.size());
        if (fileChannel.read(fileData) != fileChannel.size()) {
            throw new IOException("can't read entire file");
        }
        fileInputStream.close();
        try {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            _hash = md.digest(fileData.array());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
