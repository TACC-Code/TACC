class BackupThread extends Thread {
        public void run() {
            try {
                SelectionKey key = m_channelResponder.getChannel().register(m_selector, m_channelResponder.getChannel().validOps());
                m_channelResponder.setKey(key);
                key.attach(m_channelResponder);
            } catch (Exception e) {
                m_channelResponder.close(e);
            }
        }
}
