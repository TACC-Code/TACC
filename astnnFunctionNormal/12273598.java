class BackupThread extends Thread {
    private void init() throws IOException {
        m_inputStream = m_url.openStream();
        m_inputStreamReader = new InputStreamReader(m_inputStream);
    }
}
