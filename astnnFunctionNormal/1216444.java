class BackupThread extends Thread {
    SessionProviderFactory() {
        ExtensionClassLoader classloader = ConfigurationLoader.getExtensionClassLoader();
        try {
            Enumeration en = classloader.getResources("session.provider");
            URL url = null;
            Properties properties;
            InputStream in;
            SessionProvider provider;
            String name;
            String id;
            while (en.hasMoreElements()) {
                try {
                    url = (URL) en.nextElement();
                    in = url.openStream();
                    properties = new Properties();
                    properties.load(in);
                    IOUtil.closeStream(in);
                    if (properties.containsKey("provider.class") && properties.containsKey("provider.name")) {
                        Class cls = classloader.loadClass(properties.getProperty("provider.class"));
                        String optionsClassName = properties.getProperty("provider.options");
                        Class optionsClass = ((optionsClassName == null) || optionsClassName.equals("")) ? null : classloader.loadClass(optionsClassName);
                        String pageclass;
                        int num = 1;
                        Vector pages = new Vector();
                        do {
                            pageclass = properties.getProperty("property.page." + String.valueOf(num), null);
                            if (pageclass != null) {
                                pages.add(classloader.loadClass(pageclass));
                                num++;
                            }
                        } while (pageclass != null);
                        Class[] propertypages = new Class[pages.size()];
                        pages.toArray(propertypages);
                        name = properties.getProperty("provider.name");
                        int weight = Integer.parseInt(properties.getProperty("provider.weight"));
                        id = properties.getProperty("provider.id", name);
                        provider = new SessionProvider(id, name, cls, properties.getProperty("provider.shortdesc"), properties.getProperty("provider.mnemonic"), properties.getProperty("provider.smallicon"), properties.getProperty("provider.largeicon"), optionsClass, propertypages, weight);
                        providers.put(id, provider);
                        log.info("Installed " + provider.getName() + " session provider");
                    }
                } catch (ClassNotFoundException ex) {
                    log.warn("Session provider class not found", ex);
                } catch (IOException ex) {
                    log.warn("Failed to read " + url.toExternalForm(), ex);
                }
            }
        } catch (IOException ex) {
        }
    }
}
