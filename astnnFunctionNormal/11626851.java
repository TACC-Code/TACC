class BackupThread extends Thread {
    public static boolean isReachable(String serviceURI) throws IOException {
        URL url = new URL(serviceURI);
        HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
        httpConnection.setRequestMethod("HEAD");
        httpConnection.setConnectTimeout(2000);
        httpConnection.getResponseCode();
        return true;
    }
}
