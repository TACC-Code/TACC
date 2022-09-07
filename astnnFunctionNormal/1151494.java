class BackupThread extends Thread {
    private GoHandler getGoResponse(String goId) throws IOException, SAXException {
        String query = SearchReplace.replace(EGO_QUERY, GOID_FLAG, goId);
        URL url = new URL(myURL + query);
        URLConnection servletConnection = url.openConnection();
        servletConnection.setUseCaches(false);
        servletConnection.setDoOutput(true);
        GoHandler goHandler = null;
        InputStream inputStream = null;
        try {
            inputStream = servletConnection.getInputStream();
            goHandler = getGoResponse(inputStream);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return goHandler;
    }
}
