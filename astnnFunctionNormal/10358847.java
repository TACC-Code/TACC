class BackupThread extends Thread {
    private JTradeProperties() {
        defaultProperties = new Properties();
        userProperties = new Properties();
        File userFile = new File(System.getProperty("user.dir") + File.separator + "jtrade.properties");
        if (userFile.exists()) {
            try {
                userProperties.load(new FileInputStream(userFile));
            } catch (FileNotFoundException e) {
                ErrorMessage.handle(e);
            } catch (IOException e) {
                ErrorMessage.handle(e);
            }
        }
        URL url = ClassLoader.getSystemResource("resources/jtrade.properties");
        if (url == null) {
            ErrorMessage.handle(new NullPointerException("URL for resources/jtrade.properties not found"));
        } else {
            try {
                defaultProperties.load(url.openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
