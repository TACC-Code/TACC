class BackupThread extends Thread {
    private HttpURLConnection openConnection(String sMethod, boolean bSendData) throws MalformedURLException, IOException {
        URL url = new URL(getUrl());
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setRequestMethod(sMethod);
        httpCon.setConnectTimeout(getTimeoutMillisecs());
        httpCon.setDoInput(true);
        httpCon.setDoOutput(bSendData);
        httpCon.setUseCaches(false);
        httpCon.setRequestProperty("Connection", "Close");
        return httpCon;
    }
}
