class BackupThread extends Thread {
    @Override
    public InputStream get(String url, Map<String, String> headers) throws IOException {
        HttpGet get = new HttpGet(url);
        for (String header : headers.keySet()) {
            get.addHeader(header, headers.get(header));
        }
        HttpResponse response = this.execute(get);
        VoidResponseHandler handler = new VoidResponseHandler();
        try {
            handler.handleResponse(response);
            return new ConsumingInputStream(response);
        } catch (IOException ex) {
            get.abort();
            throw ex;
        }
    }
}
