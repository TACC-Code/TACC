class BackupThread extends Thread {
    public static String login(String username, String password) throws Throwable {
        long timeStamp = new Date().getTime();
        String preLoginUrl = "http://login.sina.com.cn/sso/prelogin.php?entry=miniblog&callback=sinaSSOController.preloginCallBack&client=ssologin.js(v1.3.19)&_=" + timeStamp;
        String result = get(preLoginUrl);
        String json = result.subSequence(35, result.length() - 1).toString();
        JSONObject obj = (JSONObject) JSONValue.parse(json);
        String servertime = obj.get("servertime").toString();
        String nonce = obj.get("nonce").toString();
        List<NameValuePair> qparams = new ArrayList<NameValuePair>();
        qparams.add(new BasicNameValuePair("entry", "weibo"));
        qparams.add(new BasicNameValuePair("gateway", "1"));
        qparams.add(new BasicNameValuePair("from", ""));
        qparams.add(new BasicNameValuePair("savestate", "7"));
        qparams.add(new BasicNameValuePair("useticket", "1"));
        qparams.add(new BasicNameValuePair("ssosimplelogin", "1"));
        qparams.add(new BasicNameValuePair("service", "miniblog"));
        qparams.add(new BasicNameValuePair("pwencode", "wsse"));
        qparams.add(new BasicNameValuePair("vsnf", "1"));
        qparams.add(new BasicNameValuePair("vsnval", ""));
        qparams.add(new BasicNameValuePair("servertime", servertime));
        qparams.add(new BasicNameValuePair("nonce", nonce));
        qparams.add(new BasicNameValuePair("encoding", HTTP.UTF_8));
        qparams.add(new BasicNameValuePair("url", "http://weibo.com/ajaxlogin.php?framelogin=1&callback=parent.sinaSSOController.feedBackUrlCallBack"));
        qparams.add(new BasicNameValuePair("returntype", "META"));
        qparams.add(new BasicNameValuePair("su", Base64.encodeBase64String(URLEncoder.encode(username, HTTP.UTF_8).getBytes())));
        qparams.add(new BasicNameValuePair("sp", new SinaSSOEncoder().encode(password, servertime, nonce)));
        UrlEncodedFormEntity params = new UrlEncodedFormEntity(qparams, HTTP.UTF_8);
        HttpPost post = getHttpPost("http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.3.19)", StringUtils.EMPTY);
        post.setEntity(params);
        HttpResponse response = client.execute(post);
        HttpEntity entity = response.getEntity();
        String location = getRedirectLocation(EntityUtils.toString(entity, HTTP.UTF_8));
        post.abort();
        String ajaxLoginResponse = get(location);
        String uuId = getUniqueid(ajaxLoginResponse);
        return uuId;
    }
}
