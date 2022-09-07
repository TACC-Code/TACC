class BackupThread extends Thread {
    @Test
    @IntegrationTest
    public void testNotifyClientSessionListenerAfterCrash() throws Exception {
        int numClients = 4;
        try {
            List<String> nodeKeys = getServiceBindingKeys(NODE_PREFIX);
            System.err.println("Node keys: " + nodeKeys);
            if (nodeKeys.isEmpty()) {
                fail("no node keys");
            } else if (nodeKeys.size() > 1) {
                fail("more than one node key");
            }
            int appPort = serverNode.getAppPort();
            for (int i = 0; i < numClients; i++) {
                String name = (i % 2 == 0) ? "client" : "badClient";
                DummyClient client = createDummyClient(name + String.valueOf(i));
                assertTrue(client.connect(appPort).login());
            }
            checkBindings(numClients);
            tearDown(false);
            String failedNodeKey = nodeKeys.get(0);
            setUp(null, false, appName, protocolVersion);
            for (DummyClient client : dummyClients.values()) {
                client.checkDisconnectedCallback(false);
            }
            Thread.sleep(WAIT_TIME);
            System.err.println("check for session bindings being removed.");
            checkBindings(0);
            nodeKeys = getServiceBindingKeys(NODE_PREFIX);
            System.err.println("Node keys: " + nodeKeys);
            if (nodeKeys.contains(failedNodeKey)) {
                fail("failed node key not removed: " + failedNodeKey);
            }
        } finally {
            for (DummyClient client : dummyClients.values()) {
                try {
                    client.disconnect();
                } catch (Exception e) {
                }
            }
        }
    }
}
