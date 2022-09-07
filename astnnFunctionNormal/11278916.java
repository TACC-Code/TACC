class BackupThread extends Thread {
    public static String post(String url, Map<String, String> paramsMap) {
        String result = null;
        HttpPost post = new HttpPost(url);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (Iterator<String> iterator = paramsMap.keySet().iterator(); iterator.hasNext(); ) {
            String name = iterator.next();
            String value = paramsMap.get(name);
            params.add(new BasicNameValuePair(name, value));
        }
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
        try {
            post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            HttpResponse response = client.execute(post);
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
        Log.d("HTTP_POST", url);
        Log.d("HTTP_POST_result", result);
        return result;
    }
}
