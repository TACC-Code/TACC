class BackupThread extends Thread {
    public void setFactories(Factory<DataPath>... factories) {
        if (writeThread != null) {
            writeThread.interrupt();
        }
        for (PercentPanel counter : counters) {
            remove(counter);
        }
        List<PercentPanel> newCounters = new LinkedList<PercentPanel>();
        for (Factory<DataPath> factory : factories) {
            if (factory == null) continue;
            newCounters.add(new PercentPanel(factory));
        }
        counters = newCounters.toArray(new PercentPanel[newCounters.size()]);
        for (PercentPanel counter : counters) {
            add(counter, gbc);
        }
        writeThread = new WriteThread();
        writeThread.start();
    }
}
