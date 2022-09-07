class BackupThread extends Thread {
    private static MQQueueManager getMQQueueManager(QueueManager qm) throws MQException {
        if (!qm.getHostName().toLowerCase().equals("localhost")) {
            MQEnvironment.hostname = qm.getHostName();
        }
        MQEnvironment.channel = qm.getChannel();
        MQEnvironment.port = qm.getPort();
        return new MQQueueManager(qm.getQManager());
    }
}
