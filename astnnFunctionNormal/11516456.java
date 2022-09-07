class BackupThread extends Thread {
    @Test
    public void testCreateCollection() {
        try {
            URL url = new URL("http://localhost:8080/exist/rest/db?_query=" + "xmldb:create-collection(%22/db/%22,%22" + TESTCASENAME + "%22)");
            url.openStream();
        } catch (Exception ex) {
            ex.printStackTrace();
            LOG.error(ex);
            fail(ex.getMessage());
        }
    }
}
