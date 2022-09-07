class BackupThread extends Thread {
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
}
