class BackupThread extends Thread {
    private static synchronized CXmlMessageResourceBundle loadPropertyFile(final String fbaseName, final Locale loc) throws MissingResourceException {
        String baseName = fbaseName;
        if (baseName == null) {
            baseName = CXmlMessageResourceBundle.CONFIG;
        }
        final String language = (loc == null) ? "" : ((("".equals(loc.getLanguage())) || (loc.getLanguage() == null)) ? "" : loc.getLanguage());
        final String country = (loc == null) ? "" : ((("".equals(loc.getCountry())) || (loc.getCountry() == null)) ? "" : ("_" + loc.getCountry().toUpperCase()));
        final String variant = (loc == null) ? "" : ((("".equals(loc.getVariant())) || (loc.getVariant() == null)) ? "" : ("_" + loc.getVariant()));
        final String fileToLoad = "".equals(language) ? ((baseName).replace('.', '/') + ".properties") : ("".equals(country) ? ((baseName + "_" + language).replace('.', '/') + ".properties") : ("".equals(variant) ? ((baseName + "_" + language + country).replace('.', '/') + ".properties") : ((baseName + "_" + language + country + variant).replace('.', '/') + ".properties")));
        final ServletContext context = CContext.getInstance().getContext();
        long lastModified = 0;
        URL urlToLoad = null;
        try {
            urlToLoad = context.getResource(fileToLoad);
        } catch (final Exception ignore) {
            urlToLoad = null;
        }
        if (urlToLoad != null) {
            try {
                final URLConnection conn = urlToLoad.openConnection();
                lastModified = conn.getLastModified();
                conn.getInputStream().close();
            } catch (final Exception ignore) {
                ;
            }
        } else {
            urlToLoad = Thread.currentThread().getContextClassLoader().getResource(fileToLoad);
            if (urlToLoad != null) {
                try {
                    final URLConnection uc = urlToLoad.openConnection();
                    lastModified = uc.getLastModified();
                    uc.getInputStream().close();
                } catch (final Exception ignore) {
                    ;
                }
            }
        }
        if (CXmlMessageResourceBundle.FileMap.containsKey(fileToLoad)) {
            final CXmlMessageResourceBundle resource = (CXmlMessageResourceBundle) CXmlMessageResourceBundle.FileMap.get(fileToLoad);
            if ((resource.lastModified == lastModified) && (lastModified != 0)) {
                return resource;
            }
        }
        if (urlToLoad == null) {
            final CXmlMessageResourceBundle resource = new CXmlMessageResourceBundle(loc, new Ci18nList("en"), baseName, lastModified);
            return resource;
        }
        final Properties prop = new Properties();
        InputStream in = null;
        try {
            in = urlToLoad.openStream();
            prop.load(in);
        } catch (final Exception ignore) {
            ignore.printStackTrace();
            final CXmlMessageResourceBundle resource = new CXmlMessageResourceBundle(loc, new Ci18nList(loc.toString()), baseName, lastModified);
            return resource;
        } finally {
            try {
                in.close();
            } catch (final Exception e) {
                ;
            }
        }
        final Ci18nList keyMap = Ci18nList.unmarshall(prop, language + country + variant);
        final CXmlMessageResourceBundle resource = new CXmlMessageResourceBundle(loc, keyMap, baseName, lastModified);
        CXmlMessageResourceBundle.FileMap.put(fileToLoad, resource);
        return resource;
    }
}
