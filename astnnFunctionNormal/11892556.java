class BackupThread extends Thread {
                public void run() {
                    while (writes.get() < operationCount && reads.get() < operationCount) {
                        boolean isGet = r.nextInt(2) == 1;
                        int operationId;
                        if (isGet) {
                            if ((operationId = reads.getAndIncrement()) < operationCount) doGet(operationId, threadId);
                        } else {
                            if ((operationId = writes.getAndIncrement()) < operationCount) doPut(operationId, threadId);
                        }
                    }
                }
}
