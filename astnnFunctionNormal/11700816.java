class BackupThread extends Thread {
    private void checkUrls(URL[] urls) throws IOException {
        assertEquals(2, urls.length);
        assertTrue(urls[0].getFile().endsWith("/impala-core/src/"));
        assertTrue(urls[1].getFile().endsWith("impala-repository/test/junit-3.8.2.jar"));
        for (URL url : urls) {
            assertNotNull(url.openConnection());
            assertNotNull(url.openStream());
        }
    }
}
