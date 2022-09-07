class BackupThread extends Thread {
    private Map executeHTTP(Map data_to_send, boolean v6) throws Exception {
        if (v6 && !enable_v6) {
            throw (new Exception("IPv6 is disabled"));
        }
        String host = getHost(v6, HTTP_SERVER_ADDRESS_V6, HTTP_SERVER_ADDRESS_V4);
        if (Logger.isEnabled()) Logger.log(new LogEvent(LOGID, "VersionCheckClient retrieving " + "version information from " + host + ":" + HTTP_SERVER_PORT + " via HTTP"));
        String url_str = "http://" + (v6 ? UrlUtils.convertIPV6Host(host) : host) + (HTTP_SERVER_PORT == 80 ? "" : (":" + HTTP_SERVER_PORT)) + "/version?";
        url_str += URLEncoder.encode(new String(BEncoder.encode(data_to_send), "ISO-8859-1"), "ISO-8859-1");
        URL url = new URL(url_str);
        HttpURLConnection url_connection = (HttpURLConnection) url.openConnection();
        url_connection.connect();
        try {
            InputStream is = url_connection.getInputStream();
            Map reply = BDecoder.decode(new BufferedInputStream(is));
            preProcessReply(reply, v6);
            return (reply);
        } finally {
            url_connection.disconnect();
        }
    }
}
