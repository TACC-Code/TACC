class BackupThread extends Thread {
    private Map<String, String> post(URL url, String payload) throws IOException {
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        urlConn.setUseCaches(false);
        urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        OutputStream out = urlConn.getOutputStream();
        YsFileUtils.writeString(out, payload);
        out.flush();
        out.close();
        return interpretResponse(urlConn);
    }
}
