class BackupThread extends Thread {
        public HttpResponse asResponse() throws IOException {
            if (client == null) {
                throw new IllegalStateException("Please specify a HttpClient instance to use for this request.");
            }
            request = createFinalRequest();
            final HttpResponse response = client.execute(request);
            return response;
        }
}
