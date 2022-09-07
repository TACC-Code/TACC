class BackupThread extends Thread {
    public void dumpStats() {
        if (STATISTICS) {
            Debug.write("Native thread ");
            Debug.write(index);
            Debug.write(": transferred out=");
            Debug.write(transferredOut);
            Debug.write("(");
            Debug.write(failedTransferOut);
            Debug.write(" failed) in=");
            Debug.writeln(transferredIn);
            Debug.write("               : chosen as least busy=");
            Debug.writeln(chosenAsLeastBusy);
            for (int i = 0; i < readyQueueLength.length; ++i) {
                Debug.write("               : average ready queue length ");
                Debug.write(i);
                Debug.write(": ");
                System.out.println((double) readyQueueLength[i] / readyQueueN);
            }
            Debug.write("               : preempted thread length=");
            System.out.println((double) preemptedThreadsLength / readyQueueN);
        }
        if (it != null && jq_InterrupterThread.STATISTICS) {
            Debug.write("Native thread ");
            Debug.write(index);
            Debug.write(": ");
            it.dumpStatistics();
        }
    }
}
