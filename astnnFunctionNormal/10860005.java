class BackupThread extends Thread {
    @Test
    public void testWithoutAcceptEncoding() throws Exception {
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(TestPortProvider.generateURL("/encoded/text"));
        HttpResponse response = client.execute(get);
        Assert.assertEquals(200, response.getStatusLine().getStatusCode());
        Assert.assertNull(response.getFirstHeader("Content-Encoding"));
        String entity = EntityUtils.toString(response.getEntity());
        Assert.assertEquals(entity, "HELLO WORLD");
    }
}
