class BackupThread extends Thread {
    public synchronized int select(long timeout) throws IOException {
        if (!isOpen()) throw new ClosedSelectorException();
        synchronized (keys) {
            synchronized (selected) {
                deregisterCancelledKeys();
                int[] read = getFDsAsArray(SelectionKey.OP_READ | SelectionKey.OP_ACCEPT);
                int[] write = getFDsAsArray(SelectionKey.OP_WRITE | SelectionKey.OP_CONNECT);
                int[] except = new int[0];
                synchronized (selectThreadMutex) {
                    if (unhandledWakeup) {
                        unhandledWakeup = false;
                        return 0;
                    } else {
                        selectThread = Thread.currentThread();
                    }
                }
                int result = 0;
                try {
                    begin();
                    result = VMSelector.select(read, write, except, timeout);
                } finally {
                    end();
                }
                synchronized (selectThreadMutex) {
                    if (unhandledWakeup) {
                        unhandledWakeup = false;
                        Thread.interrupted();
                    }
                    selectThread = null;
                }
                Iterator<SelectionKey> it = keys.iterator();
                while (it.hasNext()) {
                    int ops = 0;
                    SelectionKeyImpl key = (SelectionKeyImpl) it.next();
                    if (selected.contains(key)) {
                        ops = key.readyOps();
                    }
                    for (int i = 0; i < read.length; i++) {
                        if (key.getNativeFD() == read[i]) {
                            if (key.channel() instanceof ServerSocketChannelImpl) {
                                ops = ops | SelectionKey.OP_ACCEPT;
                            } else {
                                ops = ops | SelectionKey.OP_READ;
                            }
                        }
                    }
                    for (int i = 0; i < write.length; i++) {
                        if (key.getNativeFD() == write[i]) {
                            ops = ops | SelectionKey.OP_WRITE;
                        }
                    }
                    if (!selected.contains(key)) {
                        selected.add(key);
                    }
                    key.readyOps(key.interestOps() & ops);
                }
                deregisterCancelledKeys();
                return result;
            }
        }
    }
}
