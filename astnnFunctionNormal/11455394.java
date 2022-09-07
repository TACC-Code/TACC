class BackupThread extends Thread {
    public static void downloadFile(String appUrl) {
        URL url;
        try {
            url = new URL(appUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            int resCode = conn.getResponseCode();
            if (resCode == 200) {
                InputStream is = conn.getInputStream();
                install(is, appUrl);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
