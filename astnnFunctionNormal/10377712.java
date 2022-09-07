class BackupThread extends Thread {
    protected HttpURLConnection getHttpUrlConnection(URL urlForServlet) throws IOException {
        return (HttpURLConnection) urlForServlet.openConnection();
    }
}
