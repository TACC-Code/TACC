class BackupThread extends Thread {
    private InputSource getDTD(String type) {
        try {
            URL url = ClassLoader.getSystemResource(type);
            return new InputSource(new BufferedReader(new InputStreamReader(url.openStream())));
        } catch (Exception e) {
            logger.error("Error while trying to read DTD (" + type + "): ", e);
            return null;
        }
    }
}
