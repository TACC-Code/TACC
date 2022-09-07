class BackupThread extends Thread {
    @Test
    public void getDocumentNonExistingUser() {
        System.out.println("getDocumentNonExistingUser");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            URL url = new URL("xmldb:exist://foo:bar@localhost:8080" + "/exist/xmlrpc/db/build.xml");
            InputStream is = url.openStream();
            copyDocument(is, baos);
            is.close();
            fail("user should not exist");
        } catch (Exception ex) {
            if (!ex.getCause().getMessage().matches(".*User foo unknown.*")) {
                ex.printStackTrace();
                LOG.error(ex);
                fail(ex.getMessage());
            }
        }
    }
}
