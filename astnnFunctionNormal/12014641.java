class BackupThread extends Thread {
    public static InputStream openStream(String name) {
        try {
            URL url = new URL(name);
            return (url.openStream());
        } catch (Exception error) {
            ;
        }
        try {
            if (true) {
                URL url = Application.class.getResource("/" + name);
                if (url != null) {
                    return (url.openStream());
                }
            }
            return (new FileInputStream(name));
        } catch (Exception error) {
            logger.log(Level.SEVERE, "Error while opening stream", error);
            return (null);
        }
    }
}
