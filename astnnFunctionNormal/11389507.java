class BackupThread extends Thread {
    public static HttpURLConnection getHttpURLConnection(String urlString, int retryTimes, int retryDelay, int timeout, Properties properties, String method, IConsoleWriter writer, String label) {
        if (writer == null) writer = new DefaultConsoleWriter();
        if (label == null) label = "HttpURLConnection";
        int count = 0;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlString);
            while (conn == null && retryTimes >= count) {
                count++;
                writer.writeMessage(label, Messages.HttpClientUtils_MSG_Times + count + Messages.HttpClientUtils_MSG_Connect);
                logger.info("第" + count + "次连接...");
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(timeout);
                conn.setRequestMethod(method);
                for (Iterator it = properties.keySet().iterator(); it.hasNext(); ) {
                    String key = String.valueOf(it.next());
                    conn.setRequestProperty(key, String.valueOf(properties.get(key)));
                }
                printRequestInfo(conn, writer, label);
                int code = conn.getResponseCode();
                printResponseInfo(conn, writer, label);
                String newURLString = String.valueOf(conn.getURL());
                if (code == 404 && !urlString.equals(newURLString)) {
                    String encode = conn.getContentEncoding();
                    if (StringUtils.isEmpty(encode)) encode = IConstants.DEFAULT_ENCODING;
                    url = new URL(new String(newURLString.getBytes(encode), IConstants.FILE_ENCODING));
                    writer.writeMessage(label, Messages.HttpClientUtils_MSG_Redirect_To + url);
                    logger.info("发生重定向：" + url);
                    conn = null;
                    continue;
                }
                if (code >= 400) {
                    conn = null;
                }
                if (conn == null) {
                    writer.writeMessage(label, Messages.HttpClientUtils_ERR_Connect_Fail + retryDelay / 1000 + Messages.HttpClientUtils_MSG_Retry_After);
                    logger.info("连接失败," + retryDelay / 1000 + "秒后重试...");
                    Thread.sleep(retryDelay);
                } else {
                    writer.writeMessage(label, Messages.HttpClientUtils_MSG_Connected_Success);
                    logger.info("连接成功！");
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            writer.writeMessage(label, Messages.HttpClientUtils_ERR_URL_Error + e.getLocalizedMessage());
            logger.error("URL错误！", e);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            writer.writeMessage(label, Messages.HttpClientUtils_ERR_IO_Error + e.getLocalizedMessage());
            logger.error("I/O错误！", e);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            writer.writeMessage(label, Messages.HttpClientUtils_ERR_Error + e.getLocalizedMessage());
            logger.error("连接错误！", e);
            return null;
        }
        return conn;
    }
}
