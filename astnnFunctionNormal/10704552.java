class BackupThread extends Thread {
    private void loadConfig() {
        try {
            String configFile = getCodeBase() + "viewerapplet.config.txt";
            URL url = new URL(configFile);
            properties.load(url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
