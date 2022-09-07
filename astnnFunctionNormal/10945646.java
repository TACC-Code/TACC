class BackupThread extends Thread {
    URLConnection getConnection(String strUrl) throws IOException {
        URL url = new URL(strUrl);
        URLConnection conn = url.openConnection();
        if (!verifyHost && strUrl.startsWith("https")) {
            HttpsURLConnection httpsCon = (HttpsURLConnection) conn;
            httpsCon.setHostnameVerifier(HostnameNonVerifier);
        }
        return conn;
    }
}
