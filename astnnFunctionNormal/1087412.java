class BackupThread extends Thread {
    private String readAboutText(String urlStr) {
        String text = null;
        try {
            URL url = ClassLoader.getSystemResource(urlStr);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            StringWriter writer = new StringWriter();
            int character = reader.read();
            while (character != -1) {
                writer.write(character);
                character = reader.read();
            }
            text = writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            text = getResourceBundle().getString("standardCreditsMsg");
        }
        return text;
    }
}
