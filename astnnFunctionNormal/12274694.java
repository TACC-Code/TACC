class BackupThread extends Thread {
    private void refreshMenu() throws IOException {
        if (agent.getHostname() != null) {
            QueryAgent qagent = new QueryAgent();
            qagent.connect(agent.getHostname());
            channels = qagent.getChannels();
            qagent.disconnect();
            if (channels != null && !channels.isEmpty()) {
                setEnabled(true);
            }
        }
    }
}
