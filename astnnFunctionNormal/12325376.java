class BackupThread extends Thread {
    protected LimitedBuffer bufferResource() throws IOException {
        if (buffer == null) {
            buffer = new LimitedBuffer();
            InputStream resource = getResourceStream();
            if (resource != null) {
                byte[] copyBuffer = new byte[1024];
                int read;
                while (buffer.isValid() && (read = resource.read(copyBuffer)) > 0) {
                    buffer.write(copyBuffer, 0, read);
                }
                resource.close();
                if (buffer.isValid()) {
                    size = buffer.size();
                }
            } else {
                logger.fatal("Resource returned empty stream: " + this);
                buffer.setValid(false);
            }
        }
        return buffer;
    }
}
