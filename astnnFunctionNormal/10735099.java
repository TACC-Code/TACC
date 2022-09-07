class BackupThread extends Thread {
    public void testGetContentInputStream() {
        try {
            URL url = new URL("http://www.bk.admin.ch/themen/sprachen/00083/index.html?lang=en");
            InputStream in = url.openStream();
            Content c = provider.getContent(in);
            assertNotNull(c);
            assertEquals("Web Content", c.getType());
            assertEquals("ch.admin.topics", c.getProvider());
            assertEquals(4, c.getAttributes().size());
            assertEquals("Title", c.getAttributes().iterator().next().getName());
        } catch (MalformedURLException e) {
            fail("Malformed URL - " + e.getMessage());
        } catch (IOException e) {
            fail("Couldn't read source - " + e.getMessage());
        }
    }
}
