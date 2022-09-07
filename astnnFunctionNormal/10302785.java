class BackupThread extends Thread {
    @Test
    public void getExistingDocument() {
        System.out.println("getExistingDocument");
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            URL url = new URL("xmldb:exist://guest:guest@localhost:8080" + "/exist/xmlrpc/db/build.xml");
            InputStream is = url.openStream();
            copyDocument(is, baos);
            is.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            LOG.error(ex);
            fail(ex.getMessage());
        }
    }
}
