class BackupThread extends Thread {
    public Map<String, String> readVocab(String cacheFileName, URI source) {
        Map<String, String> vocab = new HashMap<String, String>();
        File cache = cfg.tmpFile("vocab-" + cacheFileName);
        try {
            File tmp = cfg.tmpFile(cacheFileName);
            http.downloadIfChanged(source.toURL(), tmp);
            FileUtils.copyFile(tmp, cache);
        } catch (Exception e) {
            log.warn("Cannot download vocabulary. Use cached version for " + source, e);
        }
        try {
            InputStream stream = new FileInputStream(cache);
            SAXParserFactory saxFactory = SAXParserFactory.newInstance();
            saxFactory.setNamespaceAware(true);
            saxFactory.setValidating(false);
            SAXParser p = saxFactory.newSAXParser();
            VocabHandler handler = new VocabHandler();
            p.parse(new BomSafeInputStreamWrapper(stream), handler);
            log.debug("Read " + handler.terms.size() + " vocabulary terms from " + source);
            vocab.putAll(handler.terms);
            stream.close();
        } catch (Exception e) {
            log.error("Error reading vocabulary " + source, e);
        }
        log.info(source + " vocabulary contains " + vocab.size() + " terms");
        return vocab;
    }
}
