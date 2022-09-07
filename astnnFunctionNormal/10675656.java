class BackupThread extends Thread {
    @Test
    public void test02_fail_bad_url() throws Exception {
        Logs.logMethodName();
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(baseUrl + "/esis/html/vrt/foobar.jsp");
        HttpResponse response = client.execute(get);
        try {
            assertEquals("failed code", 404, response.getStatusLine().getStatusCode());
            assertNotNull("page empty", response.getEntity());
            assertTrue("0 content length ", response.getEntity().getContentLength() > 0L);
        } finally {
            client.getConnectionManager().shutdown();
        }
    }
}
