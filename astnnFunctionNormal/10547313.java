class BackupThread extends Thread {
    @Override
    public void setup(HttpURLConnection urlConnection) throws IOException {
        String header = nonceUsable(urlConnection);
        if (header != null) {
            urlConnection.setRequestProperty("Authorization", header);
            return;
        }
        HttpURLConnection tmpUrlConnection = (HttpURLConnection) urlConnection.getURL().openConnection();
        try {
            tmpUrlConnection.getInputStream();
        } catch (IOException e) {
            if (tmpUrlConnection.getResponseCode() != 401) {
                throw new IOException(e);
            }
        }
        nonceCount = 1;
        realm = getHeaderValueByType("realm", tmpUrlConnection.getHeaderField("WWW-Authenticate"));
        nonce = getHeaderValueByType("nonce", tmpUrlConnection.getHeaderField("WWW-Authenticate"));
        algorithm = getHeaderValueByType("algorithm", tmpUrlConnection.getHeaderField("WWW-Authenticate"));
        qop = getHeaderValueByType("qop", tmpUrlConnection.getHeaderField("WWW-Authenticate"));
        charset = getHeaderValueByType("charset", tmpUrlConnection.getHeaderField("WWW-Authenticate"));
        urlConnection.setRequestProperty("Authorization", createHeader(urlConnection));
    }
}
