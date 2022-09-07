class BackupThread extends Thread {
    public String execJSP(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        String result = getReply(url.openStream());
        connection.disconnect();
        return result;
    }
}
