class BackupThread extends Thread {
    public void testGetResource_FileOutsideOfClasspath_NotFound() throws Exception {
        URL url = loader.getResource("file:" + System.currentTimeMillis());
        assertNotNull("URL should not be null", url);
        try {
            url.openStream();
            fail("should have thrown a file not found exception");
        } catch (IOException e) {
        }
    }
}
