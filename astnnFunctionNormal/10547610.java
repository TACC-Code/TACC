class BackupThread extends Thread {
    @Test
    public void test() throws Exception {
        Handler.register();
        final URL url = new URL("classpath://none/" + getClass().getName().replace('.', '/') + ".class");
        InputStream is = null;
        try {
            is = url.openConnection().getInputStream();
            Assert.assertNotNull(is);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }
}
