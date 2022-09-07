class BackupThread extends Thread {
    @Override
    public String readFile(final FileModule file) {
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, file.getPath());
        try {
            requestBuilder.sendRequest(null, new RequestCallback() {

                public void onError(Request request, Throwable exception) {
                    Log.write("failed file reading" + exception);
                }

                public void onResponseReceived(Request request, Response response) {
                    String result = response.getText();
                    file.setContent(result);
                }
            });
        } catch (RequestException e) {
            Log.write("failed file reading" + e.getMessage());
        }
        return "";
    }
}
