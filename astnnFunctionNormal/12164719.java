class BackupThread extends Thread {
    public void loadPlugins() {
        File[] oldFiles = PLUGINS_DIRECTORY.listFiles();
        if (oldFiles != null) {
            for (File file : oldFiles) {
                if (file.isDirectory()) {
                    File jarFile = new File(PLUGINS_DIRECTORY, file.getName() + ".jar");
                    if (!jarFile.exists()) {
                        uninstall(file);
                    }
                }
            }
        }
        updateClasspath();
        final URL url = getClass().getClassLoader().getResource("META-INF/plugins.xml");
        try {
            InputStreamReader reader = new InputStreamReader(url.openStream());
            loadInternalPlugins(reader);
        } catch (IOException e) {
            Log.error("Could not load plugins.xml file.");
        }
        loadPublicPlugins();
        String plugin = System.getProperty("plugin");
        if (plugin != null) {
            final StringTokenizer st = new StringTokenizer(plugin, ",", false);
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                File pluginXML = new File(token);
                loadPublicPlugin(pluginXML.getParentFile());
            }
        }
    }
}
