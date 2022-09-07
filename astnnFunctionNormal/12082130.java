class BackupThread extends Thread {
    @Test
    public void testObservers() throws Exception {
        Thread serverThread = new Thread(new Runnable() {

            public void run() {
                try {
                    in = new ObjectInputStream(new PipedInputStream(socketOutputStream));
                    out.writeObject("testNode");
                    out.writeObject(new HashMap<Integer, String>());
                    out.writeInt(TraceClientImpl.STACK_FRAME_START_ACTION);
                    out.writeLong(Thread.currentThread().getId());
                    out.writeObject(Thread.currentThread().getName());
                    out.writeObject("method signature");
                    out.writeObject(new int[0]);
                    out.writeInt(TraceClientImpl.STACK_FRAME_END_ACTION);
                    out.writeLong(Thread.currentThread().getId());
                    out.writeInt(TraceClientImpl.STACK_FRAME_EXCEPTION_ACTION);
                    out.writeLong(Thread.currentThread().getId());
                    out.writeObject("an exception");
                    out.flush();
                    action = in.readInt();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        expect(socketFactory.createSocket(node.getHost(), node.getPort())).andReturn(socket);
        socket.setSoTimeout(TraceClientImpl.DEFAULT_READ_TIMEOUT);
        expect(socket.getOutputStream()).andReturn(socketOutputStream);
        expect(socket.getInputStream()).andReturn(socketInputStream);
        traceClientObserver.connected("testNode", new HashMap<Integer, String>());
        socket.setSoTimeout(0);
        StackFrame startFrame = new StackFrame("method signature", new int[0]);
        expect(traceClientObserver.stackFrameStart(eq(serverThread.getId()), eq(serverThread.getName()), StackFrameArgumentMatcher.stackFrame(startFrame))).andReturn(null);
        traceClientObserver.stackFrameReturn(serverThread.getId());
        traceClientObserver.stackFrameException(serverThread.getId(), "an exception");
        traceClientObserver.disconnected();
        socket.close();
        control.replay();
        out = new ObjectOutputStream(new PipedOutputStream(socketInputStream));
        serverThread.start();
        Thread.sleep(250);
        client.connect(node, traceClientObserver);
        Thread.sleep(250);
        client.disconnect();
        out.close();
        in.close();
        serverThread.join(4000);
        assertEquals(TraceClientImpl.PROCEED_ACTION, action);
        client.waitForServerThread();
        control.verify();
    }
}
