class BackupThread extends Thread {
    public static final byte[] getResourceAsBytes(String resourceName) throws IOException {
        InputStream ins = getResourceAsStream(resourceName);
        if (ins == null) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] nextByte = new byte[1];
        while ((ins.read(nextByte, 0, 1)) != (-1)) baos.write(nextByte[0]);
        if (baos.size() > 0) return baos.toByteArray();
        return null;
    }
}
