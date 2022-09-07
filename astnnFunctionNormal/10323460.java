class BackupThread extends Thread {
    private InputStream execute() {
        client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        System.out.println("GET FILE: " + this.url);
        try {
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            return entity.getContent();
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }
        return null;
    }
}
