class BackupThread extends Thread {
    public static String doPost(String url, String user, String passd, Map params) {
        HttpPost httpRequest = new HttpPost(url);
        String result = "";
        if (!user.equals("") && !passd.equals("")) {
            httpRequest.setHeader("Authorization", "Basic " + Base64.encodeBytes((user + ":" + passd).getBytes()));
        }
        List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
        if (params != null) {
            Set set = params.keySet();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()) {
                if (!set.isEmpty()) {
                    list.add(new BasicNameValuePair(iterator.next().toString(), params.get(iterator.next()).toString()));
                }
            }
        }
        try {
            httpRequest.setEntity(new UrlEncodedFormEntity(list, HTTP.UTF_8));
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
            httpResponse.getEntity();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                return result = EntityUtils.toString(httpResponse.getEntity());
            } else if (httpResponse.getStatusLine().getStatusCode() == 401) {
                return result = ConfigInfo.HTTPRETURN.HTTP_ERROR_401;
            } else if (httpResponse.getStatusLine().getStatusCode() == 400) {
                return result = ConfigInfo.HTTPRETURN.HTTP_ERROR_400;
            } else if (httpResponse.getStatusLine().getStatusCode() == 404) {
                return "404";
            } else if (result.equals("")) {
                return result = ConfigInfo.HTTPRETURN.HTTPERROR;
            } else {
                return result = ConfigInfo.HTTPRETURN.COMMHTTPERRORS;
            }
        } catch (Exception e) {
            return result = ConfigInfo.HTTPRETURN.COMMHTTPERRORS;
        }
    }
}
