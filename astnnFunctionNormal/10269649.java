class BackupThread extends Thread {
    private static synchronized CXmlMessageResourceBundle loadFile(final String fbaseName, final Locale loc) throws MissingResourceException {
        String baseName = fbaseName;
        if (baseName == null) {
            baseName = CXmlMessageResourceBundle.CONFIG;
        }
        final String language = (loc == null) ? "" : ((("".equals(loc.getLanguage())) || (loc.getLanguage() == null)) ? "" : loc.getLanguage());
        final String country = (loc == null) ? "" : ((("".equals(loc.getCountry())) || (loc.getCountry() == null)) ? "" : ("_" + loc.getCountry().toUpperCase()));
        final String variant = (loc == null) ? "" : ((("".equals(loc.getVariant())) || (loc.getVariant() == null)) ? "" : ("_" + loc.getVariant()));
        final String fileToLoad = "".equals(language) ? ((baseName).replace('.', '/') + ".xml") : ("".equals(country) ? ((baseName + "_" + language).replace('.', '/') + ".xml") : ("".equals(variant) ? ((baseName + "_" + language + country).replace('.', '/') + ".xml") : ((baseName + "_" + language + country + variant).replace('.', '/') + ".xml")));
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
            if (resource.lastModified == lastModified) {
                return resource;
            }
        }
        if ((urlToLoad == null) && (loc != null) && !fileToLoad.equals(baseName + ".xml")) {
            if ((loc.getVariant() != null) && !"".equals(loc.getVariant().trim())) {
                return CXmlMessageResourceBundle.loadFile(fbaseName, new Locale(loc.getLanguage(), loc.getCountry()));
            }
            if ((loc.getCountry() != null) && !"".equals(loc.getCountry().trim())) {
                return CXmlMessageResourceBundle.loadFile(fbaseName, new Locale(loc.getLanguage()));
            }
            return CXmlMessageResourceBundle.loadFile(fbaseName, null);
        }
        if (urlToLoad == null) {
            final CXmlMessageResourceBundle resource = CXmlMessageResourceBundle.loadPropertyFile(baseName, loc);
            if (resource != null) {
                return resource;
            }
            throw new MissingResourceException("Resource file '" + baseName + "' not found for : " + loc, CXmlMessageResourceBundle.class.getName(), "");
        }
        Document doc = null;
        InputStream in = null;
        try {
            in = urlToLoad.openStream();
            doc = CDocumentBuilderFactory.newParser().newDocumentBuilder().parse(in);
        } catch (final Exception ignore) {
            throw new MissingResourceException("Resource file '" + baseName + "' not found for : " + loc, CXmlMessageResourceBundle.class.getName(), "");
        } finally {
            try {
                in.close();
            } catch (final Exception ignore) {
                ;
            }
        }
        final Ci18nList keyMap = Ci18nList.unmarshall(doc, language + country + variant);
        final CXmlMessageResourceBundle resource = new CXmlMessageResourceBundle(loc, keyMap, baseName, lastModified);
        CXmlMessageResourceBundle.FileMap.put(fileToLoad, resource);
        return resource;
    }
}
