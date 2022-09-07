class BackupThread extends Thread {
    public static junit.framework.Test suite() throws Exception {
        junit.framework.TestSuite suite = new junit.framework.TestSuite(AllUnitTests.class.getName());
        suite.addTest(TestMulticastJournalWriterInitializations.suite());
        suite.addTest(TestMulticastJournalWriterOperation.suite());
        suite.addTest(TestJournalEntrySizeEstimator.suite());
        suite.addTest(TestLocalDirectoryTransport.suite());
        suite.addTest(org.fcrepo.server.journal.readerwriter.multicast.rmi.AllUnitTests.suite());
        return suite;
    }
}
