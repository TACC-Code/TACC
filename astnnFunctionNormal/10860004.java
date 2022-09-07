class BackupThread extends Thread {
    @Test
    public void testWasZipped() throws Exception {
        HttpClient client = new DefaultHttpClient();
        {
            HttpGet get = new HttpGet(TestPortProvider.generateURL("/encoded/text"));
            get.addHeader("Accept-Encoding", "gzip, deflate");
            HttpResponse response = client.execute(get);
            Assert.assertEquals(200, response.getStatusLine().getStatusCode());
            Assert.assertEquals("gzip", response.getFirstHeader("Content-Encoding").getValue());
            String entity = EntityUtils.toString(response.getEntity());
            Assert.assertNotSame(entity, "HELLO WORLD");
        }
        {
            HttpGet get = new HttpGet(TestPortProvider.generateURL("/text"));
            get.addHeader("Accept-Encoding", "gzip, deflate");
            HttpResponse response = client.execute(get);
            Assert.assertEquals(200, response.getStatusLine().getStatusCode());
            Assert.assertEquals("gzip", response.getFirstHeader("Content-Encoding").getValue());
            String entity = EntityUtils.toString(response.getEntity());
            Assert.assertNotSame(entity, "HELLO WORLD");
        }
    }
}
