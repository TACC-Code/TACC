class BackupThread extends Thread {
    public void test_getOutputStream_AfterConnect() throws Exception {
        URL url = new URL(jettyURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.connect();
        String str_get = connection.getRequestMethod();
        assertTrue(str_get.equalsIgnoreCase("GET"));
        connection.getOutputStream();
        String str_post = connection.getRequestMethod();
        assertTrue(str_post.equalsIgnoreCase("POST"));
    }
}
