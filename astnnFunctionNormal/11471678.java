class BackupThread extends Thread {
    void downloadPhoto(Photo photo, PhotoSize size, OutputStream destination) throws IOException {
        final BufferedOutputStream out = new BufferedOutputStream(destination, IO_BUFFER_SIZE);
        final String url = photo.getUrl(size);
        final HttpGet get = new HttpGet(url);
        HttpEntity entity = null;
        try {
            final HttpResponse response = mClient.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                entity = response.getEntity();
                entity.writeTo(out);
                out.flush();
            }
        } finally {
            if (entity != null) {
                entity.consumeContent();
            }
        }
    }
}
