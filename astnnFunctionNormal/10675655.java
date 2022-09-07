class BackupThread extends Thread {
    @Test
    public void test01_basic() throws Exception {
        Logs.logMethodName();
        String[] jspURLs = { baseUrl + "/esis/module.jsp", baseUrl + "/esis/html/vrt/index.jsp", baseUrl + "/esis/html/admin/index.jsp", baseUrl + "/esis/html/asset/index.jsp", baseUrl + "/esis/html/audit/index.jsp", baseUrl + "/esis/html/portal/index.jsp", baseUrl + "/esis/html/risk/index.jsp", baseUrl + "/esis/html/mim/index.jsp", baseUrl + "/esis/reports.jsp" };
        for (int i = 0; i < jspURLs.length; ++i) {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(jspURLs[i]);
            HttpResponse response = client.execute(get);
            try {
                assertEquals("failed code for " + jspURLs[i], 200, response.getStatusLine().getStatusCode());
                assertNotNull("page empty for " + jspURLs[i], response.getEntity());
                assertTrue("0 content length " + jspURLs[i], response.getEntity().getContentLength() > 0L);
            } finally {
                client.getConnectionManager().shutdown();
            }
        }
    }
}
