class BackupThread extends Thread {
    public String exists(String URLName) {
        String url = URLName;
        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("HEAD");
            con.setConnectTimeout(10000);
            con.setReadTimeout(10000);
            if (con.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
                url = con.getHeaderField("Location");
                con = (HttpURLConnection) new URL(url).openConnection();
                con.setConnectTimeout(10000);
                con.setReadTimeout(10000);
                con.setRequestMethod("HEAD");
            }
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK || con.getResponseCode() == HttpURLConnection.HTTP_FORBIDDEN) return url;
            return null;
        } catch (SocketTimeoutException exc) {
            log.error("SocketTimeout: " + url);
            return null;
        } catch (ConnectException ce) {
            log.error("ConnectionTimeout: " + url);
            return null;
        } catch (Exception e) {
            log.error("Check URL " + url, e);
            return null;
        }
    }
}
