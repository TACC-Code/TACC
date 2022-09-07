class BackupThread extends Thread {
    public static final String execute(HttpUriRequest request, HttpClient httpClient) throws IOException {
        HttpResponse response = httpClient.execute(request);
        if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
            HttpEntity entity = response.getEntity();
            try {
                return EntityUtils.toString(entity);
            } finally {
                entity.consumeContent();
            }
        } else if (HttpStatus.SC_MOVED_TEMPORARILY == response.getStatusLine().getStatusCode()) {
            Header header = response.getFirstHeader("Location");
            if (header == null) {
                throw new IllegalStateException("Server returned redirect without Location header");
            }
            String location = header.getValue();
            request.abort();
            URI locationUri = URI.create(location);
            if (locationUri.getHost() == null) {
                locationUri = request.getURI().resolve(location);
            }
            return execute(new HttpGet(locationUri), httpClient);
        } else {
            throw new IOException("Unexpected status code [" + response.getStatusLine().getStatusCode() + "]");
        }
    }
}
