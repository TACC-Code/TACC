class BackupThread extends Thread {
    public static void loadPropertiesForClass(Properties map, Class c) {
        String key = c.getName();
        String val = SystemUtils.getSystemProperty(key, null);
        URL url;
        if (val == null) {
            val = key.replace('.', '/') + ".properties";
            url = getResource(c, val);
        } else {
            try {
                url = new URL(val);
            } catch (MalformedURLException e) {
                url = getResource(c, val);
            }
        }
        try {
            InputStream is = url.openStream();
            try {
                map.load(is);
            } finally {
                is.close();
            }
        } catch (IOException e) {
            throw new ConfigurationException("failed not load resource:", e);
        }
    }
}
