class BackupThread extends Thread {
    private void loadPlugins() {
        try {
            Enumeration e = getClass().getClassLoader().getResources(pluginConfigFile);
            while (e.hasMoreElements()) {
                Properties properties = new Properties();
                URL url = (URL) e.nextElement();
                InputStream is = url.openStream();
                try {
                    properties.load(is);
                    String factoryName = properties.getProperty(pluginFactory);
                    if (factoryName != null) {
                        P[] plugins = loadFactoryPlugins(factoryName);
                        if (plugins != null) addPlugins(plugins);
                    }
                } finally {
                    is.close();
                }
            }
            File pluginPath = pluginDirectory;
            File[] jars = pluginPath.listFiles(new FileFilter() {

                public boolean accept(File file) {
                    String fileName = file.getName().toLowerCase();
                    return (file.isFile() && fileName.endsWith(".jar"));
                }
            });
            for (int i = 0; i < jars.length; i++) {
                JarFile jarFile = new JarFile(jars[i]);
                try {
                    ZipEntry entry = jarFile.getEntry(pluginConfigFile);
                    if (entry != null) {
                        Properties properties = new Properties();
                        InputStream is = jarFile.getInputStream(entry);
                        try {
                            properties.load(is);
                            String factoryName = properties.getProperty(pluginFactory);
                            if (factoryName != null) {
                                P[] plugins = loadFactoryPlugins(factoryName, createJarClassLoader(jars[i]));
                                if (plugins != null) addPlugins(plugins);
                            }
                        } finally {
                            is.close();
                        }
                    }
                } finally {
                    jarFile.close();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
