class BackupThread extends Thread {
    private Object unzipObject(byte[] bytes) throws Exception {
        ByteArrayInputStream inBuffer = new ByteArrayInputStream(bytes);
        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
        GZIPInputStream gzip = new GZIPInputStream(inBuffer);
        byte[] tmpBuffer = new byte[1024];
        int n;
        while ((n = gzip.read(tmpBuffer)) >= 0) outBuffer.write(tmpBuffer, 0, n);
        gzip.close();
        ByteArrayInputStream input = new ByteArrayInputStream(outBuffer.toByteArray());
        ObjectInputStream objectInput = new ObjectInputStream(input);
        E e = (E) objectInput.readObject();
        objectInput.close();
        return e;
    }
}
