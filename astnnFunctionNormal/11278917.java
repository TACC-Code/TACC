class BackupThread extends Thread {
    public static String get(String url) {
        String result = null;
        DefaultHttpClient client = new DefaultHttpClient(httpParams);
        if (HttpUtil.TICKET != null) {
            CookieStore cookieStore = new BasicCookieStore();
            BasicClientCookie cookie = new BasicClientCookie(TICKET_NAME, HttpUtil.TICKET);
            cookie.setDomain(DOMAIN);
            cookie.setPath("/");
            cookieStore.addCookie(cookie);
            cookie.getDomain();
            client.setCookieStore(cookieStore);
        }
        HttpGet get = new HttpGet(url);
        try {
            HttpResponse response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity());
            } else {
                result = ERROR_START + response.getStatusLine().toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.getConnectionManager().shutdown();
        }
        Log.d("HTTP_GET", url);
        Log.d("HTTP_GET_result", result);
        return result;
    }
}
