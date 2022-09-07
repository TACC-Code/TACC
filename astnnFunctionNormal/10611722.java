class BackupThread extends Thread {
    public InputStreamReader getURL(String streamName) throws IOException, MalformedURLException {
        URL url = null;
        if (m_getTemplateFromResources) {
            url = m_documentBase.getClass().getResource(m_resourcesTemplatePathString + streamName);
        }
        if (url == null) {
            url = new URL(m_documentBase, m_URLRoot + streamName);
        }
        URLConnection urlConnection = url.openConnection();
        urlConnection.setDoOutput(false);
        urlConnection.setUseCaches(false);
        return new InputStreamReader((urlConnection.getInputStream()));
    }
}
