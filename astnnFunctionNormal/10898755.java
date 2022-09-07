class BackupThread extends Thread {
    private void doGet() {
        try {
            URL url = getURL();
            URLConnection conn = url.openConnection();
            setHeaders(conn);
            response = new ClientResponse(conn);
        } catch (Exception e) {
            response = new ClientResponse(e);
        } finally {
            if (callback != null) {
                callback.run(response);
            }
        }
    }
}
