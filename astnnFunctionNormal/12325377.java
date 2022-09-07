class BackupThread extends Thread {
    public final void write(Device out) throws IOException {
        if (buffer == null) {
            bufferResource();
            if (buffer == null) return;
        }
        if (buffer.isValid()) {
            buffer.writeTo(out);
        } else {
            InputStream resource = getResourceStream();
            if (resource != null) {
                int deliverSize = 0;
                byte[] copyBuffer = new byte[1024];
                int read;
                while ((read = resource.read(copyBuffer)) > 0) {
                    out.write(copyBuffer, 0, read);
                    deliverSize += read;
                }
                resource.close();
                size = deliverSize;
            }
        }
        out.flush();
    }
}
