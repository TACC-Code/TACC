class BackupThread extends Thread {
    public static VersionNumber getVersion() {
        if (version == null) {
            try {
                URL url = getExtensionLoader().getResource("org/oboedit/resources/VERSION");
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                version = new VersionNumber(reader.readLine());
                reader.close();
            } catch (Exception e) {
                try {
                    version = new VersionNumber("0.0");
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return version;
    }
}
