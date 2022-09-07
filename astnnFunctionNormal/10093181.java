class BackupThread extends Thread {
    private static List<SkinsNode> _getMetaInfSkinsNodeList() {
        List<SkinsNode> allSkinsNodes = new ArrayList<SkinsNode>();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            Enumeration<URL> urls = loader.getResources(_META_INF_CONFIG_FILE);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                _LOG.finest("Processing:{0}", url);
                try {
                    InputStream in = url.openStream();
                    if (in != null) {
                        SkinsNode metaInfSkinsNode = _getSkinsNodeFromInputStream(null, null, in, _getDefaultManager(), _META_INF_CONFIG_FILE);
                        allSkinsNodes.add(metaInfSkinsNode);
                        in.close();
                    }
                } catch (Exception e) {
                    _LOG.warning("ERR_PARSING", url);
                    _LOG.warning(e);
                }
            }
        } catch (IOException e) {
            _LOG.severe("ERR_LOADING_FILE", _META_INF_CONFIG_FILE);
            _LOG.severe(e);
        }
        return allSkinsNodes;
    }
}
