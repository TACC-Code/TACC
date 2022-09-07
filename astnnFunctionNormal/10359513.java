class BackupThread extends Thread {
    public <T> T readObject(ByteBuffer data, Class<T> c) throws IOException {
        try {
            GZIPCompressedMessage result = new GZIPCompressedMessage();
            byte[] byteArray = new byte[data.remaining()];
            data.get(byteArray);
            GZIPInputStream in = new GZIPInputStream(new ByteArrayInputStream(byteArray));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] tmp = new byte[9012];
            int read;
            while (in.available() > 0 && ((read = in.read(tmp)) > 0)) {
                out.write(tmp, 0, read);
            }
            result.setMessage((Message) Serializer.readClassAndObject(ByteBuffer.wrap(out.toByteArray())));
            return (T) result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e.toString());
        }
    }
}
