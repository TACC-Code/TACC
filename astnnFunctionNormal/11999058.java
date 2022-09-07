class BackupThread extends Thread {
    private static final void load(Map labels, URL url, String charset) throws IOException {
        log.info(MCommon.FILE_OPENING, url);
        final Map news = new HashMap();
        final InputStream is = url.openStream();
        try {
            Maps.load(news, is, charset);
        } finally {
            try {
                is.close();
            } catch (Throwable ex) {
            }
        }
        for (Iterator it = news.entrySet().iterator(); it.hasNext(); ) {
            final Map.Entry me = (Map.Entry) it.next();
            final Object key = me.getKey();
            if (labels.put(key, me.getValue()) != null) log.warning("Label of " + key + " is replaced by " + url);
        }
    }
}
