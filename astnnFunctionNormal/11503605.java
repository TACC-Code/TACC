class BackupThread extends Thread {
    public static junit.framework.Test suite() throws Exception {
        junit.framework.TestSuite suite = new junit.framework.TestSuite(AllIntegrationTests.class.getName());
        suite.addTest(org.fcrepo.server.journal.readerwriter.AllIntegrationTests.suite());
        return suite;
    }
}
