class BackupThread extends Thread {
    public void testRelay() throws Exception {
        try {
            ChatModel cm1 = new ChatModel(22345, null);
            ICommFacade commFacade = cm1.getCommFacade();
            DummyUserInterface dummyUI = new DummyUserInterface();
            cm1.startSelectionProcess();
            cm1.setUserInterface(dummyUI);
            IPeer peer1 = cm1.getMyself().getPeer();
            IConnectionBroker connBroker = ConnectionManagerFactory.createConnectionBroker(new DummyCallback(cm1.getMyself().getPeerID()));
            SocketChannel sc = connBroker.requestTCPChannel(peer1, ConnectionRequestType.FORCE_RELAY);
            sc.configureBlocking(false);
            commFacade.getChannelManager().registerChannel(sc);
            commFacade.getChannelManager().setSelectedOps(sc, SelectionKey.OP_READ);
            assertTrue(sc != null);
            SendFileRequest sfr = new SendFileRequest(peer1.getPeerID(), null, "bl", new File("testing/test.jpg"));
            commFacade.getMessageProcessor().enableHooks(true);
            MessageHook hook = new MessageHook(sfr);
            commFacade.getMessageProcessor().installHook(hook, hook.getPredicate());
            commFacade.sendTCPMessage(sc, sfr);
            IEnvelope envelope = hook.waitForEnvelope(15);
            assertTrue(envelope != null);
            cm1.shutdown();
        } catch (BindException e) {
            e.printStackTrace();
            assertTrue("Got a bindexception", false);
        }
    }
}
