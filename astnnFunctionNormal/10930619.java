class BackupThread extends Thread {
    public void testLogonAfterServerDisconnect() throws Exception {
        final WriteCounter initiatorWriteCounter = new WriteCounter("initiator");
        ServerThread serverThread = new ServerThread();
        try {
            serverThread.start();
            serverThread.waitForInitialization();
            SessionID serverSessionID = new SessionID(FixVersions.BEGINSTRING_FIX42, "ISLD", "TW");
            Session serverSession = Session.lookupSession(serverSessionID);
            SessionID clientSessionID = new SessionID(FixVersions.BEGINSTRING_FIX42, "TW", "ISLD");
            SessionSettings settings = getClientSessionSettings(clientSessionID);
            ClientApplication clientApplication = new ClientApplication();
            ThreadedSocketInitiator initiator = new ThreadedSocketInitiator(clientApplication, new MemoryStoreFactory(), settings, new DefaultMessageFactory());
            initiator.setIoFilterChainBuilder(new IoFilterChainBuilder() {

                public void buildFilterChain(IoFilterChain chain) throws Exception {
                    chain.addLast("TestFilter", initiatorWriteCounter);
                }
            });
            try {
                log.info("Do first login");
                clientApplication.setUpLogonExpectation();
                initiator.start();
                Session clientSession = Session.lookupSession(clientSessionID);
                assertLoggedOn(clientApplication, clientSession);
                log.info("Disconnect from server-side and assert that client session " + "reconnects and logs on properly");
                clientApplication.setUpLogonExpectation();
                serverSession.disconnect("Server side disconnection for testing", false);
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(100L);
                    if (serverSession.hasResponder()) {
                        break;
                    }
                }
                assertLoggedOn(clientApplication, clientSession);
            } finally {
                initiator.stop();
            }
        } finally {
            serverThread.interrupt();
            serverThread.join();
        }
        assertTrue("Initiator write count = 0, filter problem?", initiatorWriteCounter.getCount() > 0);
        assertTrue("Acceptor write count = 0, filter problem?", serverThread.getWriteCount() > 0);
        assertTrue("Initiator sessionCreated not called", initiatorWriteCounter.wasSessionCreatedCalled());
        assertTrue("Acceptor sessionCreated not called", serverThread.wasSessionCreatedCalled());
    }
}
