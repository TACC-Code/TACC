class BackupThread extends Thread {
    public RemoteConfigReader() {
        try {
            url = new URL(urlString);
            urlConn = url.openConnection();
            inputStream = urlConn.getInputStream();
            logManager = LogManager.getLogManager();
            logManager.readConfiguration(inputStream);
        } catch (MalformedURLException mue) {
            System.err.println("Could not open url:" + urlString);
        } catch (IOException ioe) {
            System.err.println("IOException occured in reading:" + urlString);
        } catch (SecurityException se) {
            System.err.println("Security exception occured in class RemoteConfigReader");
        }
    }
}
