class BackupThread extends Thread {
    private static void find(String className, ICache cache, String variation) throws SAXException, IOException {
        synchronized (cache) {
            if (cache.containsKey(className)) return;
            CustomHandler handler = new CustomHandler(className);
            try {
                String xmlFile;
                if (VariationContext.DEFAULT_VARIATION.equals(variation)) xmlFile = (String) Config.getProperty(Config.PROP_RULES_ENGINE_CORE_RULES_URL, DEFAULT_XML_FILE); else {
                    String variationRulesDirectory = (String) Config.getProperty(Config.PROP_RULES_ENGINE_VARIATIONS_DIR, null);
                    if (variationRulesDirectory == null || variationRulesDirectory.length() == 0) {
                        return;
                    } else {
                        xmlFile = variationRulesDirectory + '/' + variation + VARIATION_FILE_SUFFIX;
                    }
                }
                URL url = URLHelper.newExtendedURL(xmlFile);
                if (url == null) throw new FileNotFoundException("File not found - " + xmlFile);
                XMLReader reader = XMLReaderFactory.createXMLReader(PARSER_NAME);
                reader.setContentHandler(handler);
                reader.setEntityResolver(new DefaultEntityResolver());
                reader.setErrorHandler(new DefaultErrorHandler());
                reader.parse(new InputSource(url.openStream()));
                cache.put(className, handler.getClassMetaData());
            } catch (SAXException e) {
                if (SUCCESS_MESSAGE.equals(e.getMessage())) cache.put(className, handler.getClassMetaData()); else throw e;
            } catch (IOException e) {
                throw e;
            }
        }
    }
}
