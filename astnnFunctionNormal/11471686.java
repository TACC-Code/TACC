class BackupThread extends Thread {
    private void executeRequest(HttpGet get, ResponseHandler handler) throws IOException {
        HttpEntity entity = null;
        HttpHost host = new HttpHost(API_REST_HOST, 80, "http");
        try {
            final HttpResponse response = mClient.execute(host, get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                entity = response.getEntity();
                final InputStream in = entity.getContent();
                handler.handleResponse(in);
            }
        } finally {
            if (entity != null) {
                entity.consumeContent();
            }
        }
    }
}
