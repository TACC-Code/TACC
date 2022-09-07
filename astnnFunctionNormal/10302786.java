class BackupThread extends Thread {
    @Test
    public void getNonExistingDocument() {
        System.out.println("getNonExistingDocument");
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            URL url = new URL("xmldb:exist://guest:guest@localhost:8080" + "/exist/xmlrpc/db/foobar/build.xml");
            InputStream is = url.openStream();
            copyDocument(is, baos);
            is.close();
            fail("Document should not exist");
        } catch (Exception ex) {
            if (!ex.getCause().getMessage().matches(".*Collection /db/foobar not found!.*")) {
                ex.printStackTrace();
                LOG.error(ex);
                fail(ex.getMessage());
            }
        }
    }
}
