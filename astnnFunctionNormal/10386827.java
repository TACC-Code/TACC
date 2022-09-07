class BackupThread extends Thread {
        public void run() {
            LinkedList localQueue = null;
            try {
                while (true) {
                    synchronized (mux.muxLock) {
                        while (!mux.muxDown && sendQueue.size() == 0) {
                            mux.muxLock.wait();
                        }
                        if (mux.muxDown && sendQueue.size() == 0) {
                            logger.log(Level.FINEST, "mux writer thread dying, connection " + "down and nothing more to send");
                            break;
                        }
                        localQueue = sendQueue;
                        sendQueue = new LinkedList();
                    }
                    boolean needToFlush = false;
                    while (!localQueue.isEmpty()) {
                        Object next = localQueue.getFirst();
                        if (next instanceof ByteBuffer) {
                            outChannel.write((ByteBuffer) next);
                            needToFlush = true;
                        } else {
                            assert next instanceof IOFuture;
                            if (needToFlush) {
                                out.flush();
                                needToFlush = false;
                            }
                            ((IOFuture) next).done();
                        }
                        localQueue.removeFirst();
                    }
                    if (needToFlush) {
                        out.flush();
                    }
                }
            } catch (InterruptedException e) {
                try {
                    logger.log(Level.WARNING, "mux writer thread dying, interrupted", e);
                } catch (Throwable t) {
                }
                mux.setDown("mux writer thread interrupted", e);
            } catch (IOException e) {
                try {
                    logger.log(Levels.HANDLED, "mux writer thread dying, I/O error", e);
                } catch (Throwable t) {
                }
                mux.setDown("I/O error writing to mux connection: " + e.toString(), e);
            } catch (Throwable t) {
                try {
                    logger.log(Level.WARNING, "mux writer thread dying, unexpected exception", t);
                } catch (Throwable tt) {
                }
                mux.setDown("unexpected exception in mux writer thread: " + t.toString(), t);
            } finally {
                synchronized (mux.muxLock) {
                    assert mux.muxDown;
                    if (localQueue != null) {
                        drainQueue(localQueue);
                    }
                    drainQueue(sendQueue);
                }
                try {
                    outChannel.close();
                } catch (IOException e) {
                }
            }
        }
}
