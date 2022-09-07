class BackupThread extends Thread {
    public void executeGET(String cookies) throws MalformedURLException, ProtocolException, IOException {
        url = new URL(protocol, gameHost, request);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Language", "en-US");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-GB; rv:1.9.0.5) Gecko/2008120122 Firefox/3.0.5");
        conn.setRequestProperty("Accept", "text/javascript, text/html, application/xml, text/xml");
        conn.setRequestProperty("Referer", "http://www.volvooceanracegame.org/home.php");
        if (null != cookies) conn.setRequestProperty("Cookie", cookies);
        conn.setUseCaches(false);
        conn.setDoInput(false);
        conn.setDoOutput(true);
    }
}
