class BackupThread extends Thread {
    private static Document getConfig(URL url, EntityResolver entityResolver) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("getConfig(URL, EntityResolver) - start");
        }
        try {
            SAXBuilder builder = new SAXBuilder(true);
            builder.setEntityResolver(entityResolver);
            Document genieDoc = builder.build(url.openStream());
            if (log.isDebugEnabled()) {
                log.debug("getConfig(URL, EntityResolver) - end");
            }
            return genieDoc;
        } catch (Exception e) {
            log.error("getConfig(URL, EntityResolver)", e);
            throw e;
        }
    }
}
