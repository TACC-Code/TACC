class BackupThread extends Thread {
    @Test
    public void test06_fail_bad_session_id() throws Exception {
        Logs.logMethodName();
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(baseUrl + "/esis/html/vrt/vuln_email.jsp?e_vulnerability_id=1080");
        get.addHeader("Session-Id", Long.toString(Long.MAX_VALUE) + "99999");
        HttpResponse response = client.execute(get);
        try {
            assertEquals("failed code", 500, response.getStatusLine().getStatusCode());
        } finally {
            client.getConnectionManager().shutdown();
        }
    }
}
