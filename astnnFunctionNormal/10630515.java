class BackupThread extends Thread {
    public static boolean hasUpdate() {
        boolean toUpdate = false;
        if (InternetChecker.check()) {
            DesktopContext context = Desktop.getInstance().getDesktopContext();
            String website = context.getProperty("pride.inspector.website");
            String name = context.getProperty("pride.inspector.name");
            String version = context.getProperty("pride.inspector.version");
            BufferedReader reader = null;
            try {
                URL url = new URL(website + "/downloads/detail?name=" + name + "-" + version + ".zip");
                int response = ((HttpURLConnection) url.openConnection()).getResponseCode();
                if (response == 404) {
                    toUpdate = true;
                } else {
                    reader = new BufferedReader(new InputStreamReader(url.openStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.toLowerCase().contains("label:deprecated")) {
                            toUpdate = true;
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                logger.warn("Failed to check for updates", e);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        logger.warn("Failed to check for updates");
                    }
                }
            }
        }
        return toUpdate;
    }
}
