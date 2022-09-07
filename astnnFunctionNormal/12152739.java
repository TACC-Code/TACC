class BackupThread extends Thread {
    public static byte[] connection(String sUrl, String host, String cookie, String parametc) {
        URL url = null;
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            url = new URL(sUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setInstanceFollowRedirects(false);
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows 2000)");
            if (!SpiderUtil.isStringNull(cookie)) {
                connection.addRequestProperty("Cookie", cookie);
                connection.setRequestMethod("GET");
                connection.setFollowRedirects(true);
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setUseCaches(false);
                connection.setAllowUserInteraction(false);
                connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows 2000)");
                connection.setRequestProperty("Accept", "*/*");
                connection.setRequestProperty("accept-language", "zh-cn");
                connection.setRequestProperty("Cache-Control", "no-cache");
                connection.setRequestProperty("Pragma", "no-cache");
                connection.setRequestProperty("Host", host);
                connection.setRequestProperty("Connection", "keep-alive");
                connection.setAllowUserInteraction(false);
            }
            if (parametc.length() > 0) {
                OutputStreamWriter outwr = new OutputStreamWriter(connection.getOutputStream());
                outwr.write(parametc);
                outwr.flush();
            }
            connection.setConnectTimeout(90 * 1000);
            connection.setReadTimeout(90 * 1000);
            connection.connect();
            inputStream = new BufferedInputStream(connection.getInputStream());
            InputStream is = new BufferedInputStream(inputStream);
            try {
                byte bytes[] = new byte[40960];
                int nRead = -1;
                while ((nRead = is.read(bytes, 0, 40960)) > 0) {
                    os.write(bytes, 0, nRead);
                }
                os.close();
                is.close();
                inputStream.close();
            } catch (SocketTimeoutException e) {
                os.close();
                is.close();
                inputStream.close();
            } catch (IOException e) {
                os.close();
                is.close();
                inputStream.close();
            }
        } catch (Exception e) {
        }
        return os.toByteArray();
    }
}
