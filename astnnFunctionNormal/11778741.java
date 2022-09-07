class BackupThread extends Thread {
    private void loadUrl(String url) {
        try {
            System.out.println("url: " + url);
            con = (HttpURLConnection) new URL(url).openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setConnectTimeout(mlisecondstimeout);
            con.setRequestMethod(method);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
