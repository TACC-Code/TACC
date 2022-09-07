class BackupThread extends Thread {
    public String doGet(String getUrl) throws IOException {
        HttpGet httpget = new HttpGet(getUrl);
        System.out.println("Get Url:" + httpget.getURI());
        HttpResponse response = httpclient.execute(httpget);
        System.out.println("Status line: " + response.getStatusLine());
        System.out.println("Header: " + getResponseStr(response));
        System.out.println("Cookie: " + getCookiesStr());
        HttpEntity entity = response.getEntity();
        return EntityUtils.toString(entity);
    }
}
