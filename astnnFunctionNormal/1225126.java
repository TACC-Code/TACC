class BackupThread extends Thread {
    protected void sendMessagesFromNodesToClient(final DummyClient client, int numAdditionalNodes, int iterations, final Delivery delivery, final boolean oneUnreliableServer) throws Exception {
        final String counterName = "counter";
        client.connect(serverNode.getAppPort());
        assertTrue(client.login());
        for (int i = 0; i < numAdditionalNodes; i++) {
            addNodes(Integer.toString(i));
        }
        final List<SgsTestNode> nodes = new ArrayList<SgsTestNode>();
        nodes.add(serverNode);
        nodes.addAll(additionalNodes.values());
        int numExpectedMessages = oneUnreliableServer ? iterations : nodes.size() * iterations;
        txnScheduler.runTask(new TestAbstractKernelRunnable() {

            @SuppressWarnings("unchecked")
            public void run() {
                boolean setUnreliableServer = oneUnreliableServer;
                for (SgsTestNode node : nodes) {
                    String key = "com.sun.sgs.impl.service.session.server." + node.getNodeId();
                    ClientSessionServer sessionServer = ((ManagedSerializable<ClientSessionServer>) dataService.getServiceBinding(key)).get();
                    InvocationHandler handler;
                    if (setUnreliableServer) {
                        handler = new HungryInvocationHandler(sessionServer);
                        setUnreliableServer = false;
                    } else {
                        handler = new DelayingInvocationHandler(sessionServer);
                    }
                    ClientSessionServer delayingServer = (ClientSessionServer) Proxy.newProxyInstance(ClientSessionServer.class.getClassLoader(), new Class[] { ClientSessionServer.class }, handler);
                    dataService.setServiceBinding(key, new ManagedSerializable(delayingServer));
                }
            }
        }, taskOwner);
        for (int i = 0; i < iterations; i++) {
            for (SgsTestNode node : nodes) {
                TransactionScheduler localTxnScheduler = node.getSystemRegistry().getComponent(TransactionScheduler.class);
                Identity identity = node.getProxy().getCurrentOwner();
                localTxnScheduler.scheduleTask(new TestAbstractKernelRunnable() {

                    public void run() {
                        DataManager dataManager = AppContext.getDataManager();
                        Counter counter;
                        try {
                            counter = (Counter) dataManager.getBinding(counterName);
                        } catch (NameNotBoundException e) {
                            throw new MaybeRetryException("retry", true);
                        }
                        ClientSession session = (ClientSession) dataManager.getBinding(client.name);
                        MessageBuffer buf = new MessageBuffer(4);
                        buf.putInt(counter.getAndIncrement());
                        session.send(ByteBuffer.wrap(buf.getBuffer()), delivery);
                    }
                }, identity);
            }
        }
        txnScheduler.runTask(new TestAbstractKernelRunnable() {

            public void run() {
                AppContext.getDataManager().setBinding(counterName, new Counter());
            }
        }, taskOwner);
        client.waitForClientToReceiveExpectedMessages(numExpectedMessages);
    }
}
