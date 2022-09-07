class BackupThread extends Thread {
    public static String get(String url) throws Throwable {
        HttpGet get = new HttpGet(url);
        HttpResponse response = client.execute(get);
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity, HTTP.UTF_8);
        get.abort();
        System.out.println("INFO get method: " + url + "\ncontentLength:" + result.length());
        return result;
    }
}
