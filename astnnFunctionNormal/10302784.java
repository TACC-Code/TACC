class BackupThread extends Thread {
    @Test
    public void putDocumentToExistingNotExistingCollection() {
        System.out.println("putDocumentToExistingNotExistingCollection");
        try {
            URL url = new URL("xmldb:exist://guest:guest@localhost:8080" + "/exist/xmlrpc/db/foobar/build.xml");
            OutputStream os = url.openConnection().getOutputStream();
            FileInputStream is = new FileInputStream("build.xml");
            copyDocument(is, os);
            is.close();
            os.close();
        } catch (Exception ex) {
            if (!ex.getCause().getMessage().matches(".*Collection /db/foobar not found.*")) {
                ex.printStackTrace();
                LOG.error(ex);
                fail(ex.getMessage());
            }
        }
    }
}
