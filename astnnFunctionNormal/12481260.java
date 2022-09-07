class BackupThread extends Thread {
    @Override
    protected List<ClassFetcher> find0(ExceptionHandler handler) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            InputStream stream = url.openStream();
            try {
                byte[] buffer = new byte[PACKET_SIZE];
                int bytesRead;
                while ((bytesRead = stream.read(buffer, 0, buffer.length)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
            } finally {
                stream.close();
            }
            byte[] data = output.toByteArray();
            try {
                return Collections.singletonList(ClassFetcher.getFetcher(url, data));
            } catch (UnsupportedSourceException e) {
                handler.handle(e);
                return Collections.emptyList();
            }
        } catch (IOException e) {
            handler.handle(e);
            return Collections.emptyList();
        }
    }
}
