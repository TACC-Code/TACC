class BackupThread extends Thread {
    protected Response(final HttpEntity entity, final URL url) throws IOException {
        this.entity = entity;
        this.url = url;
        uc = entity == null ? url.openConnection() : null;
    }
}
