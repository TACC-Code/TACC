class BackupThread extends Thread {
    public Map<String, Object> doPost(String postUrl, List<NameValuePair> params) throws UnsupportedEncodingException, IOException {
        UrlEncodedFormEntity requestEntity = new UrlEncodedFormEntity(params, "UTF-8");
        HttpPost httppost = new HttpPost(postUrl);
        httppost.setEntity(requestEntity);
        HttpResponse response = httpclient.execute(httppost);
        System.out.println("Status line: " + response.getStatusLine());
        System.out.println("Header: " + getResponseStr(response));
        System.out.println("Cookie: " + getCookiesStr());
        HttpEntity entity = response.getEntity();
        Map<String, Object> resultMap = new HashMap();
        resultMap.put("content", EntityUtils.toString(entity));
        return resultMap;
    }
}
