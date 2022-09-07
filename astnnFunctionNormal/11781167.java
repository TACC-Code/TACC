class BackupThread extends Thread {
    public void setSource(URL url) throws IOException {
        m_structure = null;
        setRetrieval(NONE);
        setSource(url.openStream());
        m_URL = url.toString();
        m_File = null;
    }
}
