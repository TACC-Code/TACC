class BackupThread extends Thread {
    protected XDataInput createStream(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(in.available());
        for (int b = in.read(); b != -1; b = in.read()) out.write(b);
        return new ByteBufferInputStream(ByteBuffer.wrap(out.toByteArray()));
    }
}
