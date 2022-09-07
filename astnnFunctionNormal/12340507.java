class BackupThread extends Thread {
    public static Project createProject(final URL url) throws IOException, ParserConfigurationException, SAXException {
        InputStream is = null;
        try {
            is = url.openStream();
            Project prj = createProject(is);
            return prj;
        } finally {
            if (is != null) {
                try {
                    is.close();
                    is = null;
                } catch (IOException e) {
                }
            }
        }
    }
}
