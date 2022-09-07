class BackupThread extends Thread {
    private void sendBufferToClient(final ByteBuffer buf, final String expectedMsgString, final Delivery delivery) throws Exception {
        final String name = "dummy";
        DummyClient client = createDummyClient(name);
        try {
            client.connect(serverNode.getAppPort());
            assertTrue(client.login());
            final int numMessages = 3;
            for (int i = 0; i < numMessages; i++) {
                txnScheduler.runTask(new TestAbstractKernelRunnable() {

                    public void run() {
                        ClientSession session = (ClientSession) AppContext.getDataManager().getBinding(name);
                        System.err.println("Sending messages");
                        session.send(buf, delivery);
                    }
                }, taskOwner);
            }
            System.err.println("waiting for client to receive messages");
            client.waitForClientToReceiveExpectedMessages(numMessages);
            for (byte[] message : client.clientReceivedMessages) {
                if (message.length == 0) {
                    fail("message buffer emtpy");
                }
                String msgString = (new MessageBuffer(message)).getString();
                if (!msgString.equals(expectedMsgString)) {
                    fail("expected: " + expectedMsgString + ", received: " + msgString);
                } else {
                    System.err.println("received expected message: " + msgString);
                }
            }
        } finally {
            client.disconnect();
        }
    }
}
