class BackupThread extends Thread {
    public InputStream createInputStream() throws IOException {
        InputStream stream = null;
        URL url = this.getValue();
        if (url != null) {
            stream = url.openStream();
        }
        return stream;
    }
}
