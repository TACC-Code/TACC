class BackupThread extends Thread {
    @Test
    public void test03_fail_session_id() throws Exception {
        Logs.logMethodName();
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(baseUrl + "/esis/html/vrt/vuln_email.jsp?e_vulnerability_id=1080");
        HttpResponse response = client.execute(get);
        try {
            assertEquals("failed code", 500, response.getStatusLine().getStatusCode());
            assertNotNull("page empty", response.getEntity());
        } finally {
            client.getConnectionManager().shutdown();
        }
    }
}
