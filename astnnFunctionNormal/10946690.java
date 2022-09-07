class BackupThread extends Thread {
    public String getChannelVariable(String name) {
        if (name == null || "".equals(name)) {
            return null;
        }
        return m_channelVars.get(name);
    }
}
