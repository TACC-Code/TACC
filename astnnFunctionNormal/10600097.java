class BackupThread extends Thread {
    @Override
    protected void doClose() throws IOException {
        try {
            HttpRequest request = httpClient.createRequest(HttpRequest.Method.POST, "/connections/" + connectionId);
            HttpResponse response = request.execute();
            if (response.getStatusCode() == HttpConstants.StatusCodes.NO_CONTENT) {
                return;
            } else {
                throw Util.createException(response);
            }
        } catch (HttpException ex) {
            throw new VCHConnectionException(ex);
        }
    }
}
