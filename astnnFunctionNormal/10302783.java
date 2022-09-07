class BackupThread extends Thread {
    @Test
    public void putDocumentToExistingCollection() {
        System.out.println("putDocumentToExistingCollection");
        try {
            URL url = new URL("xmldb:exist://guest:guest@localhost:8080" + "/exist/xmlrpc/db/build.xml");
            OutputStream os = url.openConnection().getOutputStream();
            FileInputStream is = new FileInputStream("build.xml");
            copyDocument(is, os);
            is.close();
            os.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            LOG.error(ex);
            fail(ex.getMessage());
        }
    }
}
