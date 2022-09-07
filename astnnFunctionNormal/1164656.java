class BackupThread extends Thread {
    public void search(ISearchQuery searchQuery, ISearchHitCollector collector, IProgressMonitor pm) throws QueryTooComplexException {
        pm.beginTask("", 100);
        PreferenceFileHandler prefHandler = new PreferenceFileHandler();
        String host[] = prefHandler.getHostEntries();
        String port[] = prefHandler.getPortEntries();
        String path[] = prefHandler.getPathEntries();
        String isEnabled[] = prefHandler.isEnabled();
        try {
            if (RemoteHelp.isEnabled()) {
                int numICs = host.length;
                for (int i = 0; i < numICs; i++) {
                    if (isEnabled[i].equals("true")) {
                        InputStream in = null;
                        try {
                            URL url = new URL("http", host[i], new Integer(port[i]).intValue(), path[i] + PATH_SEARCH + '?' + PARAM_PHRASE + '=' + URLCoder.encode(searchQuery.getSearchWord()) + '&' + PARAM_LANG + '=' + searchQuery.getLocale());
                            in = url.openStream();
                            if (parser == null) {
                                parser = new RemoteSearchParser();
                            }
                            List hits = parser.parse(in, new SubProgressMonitor(pm, 100));
                            collector.addHits(hits, null);
                        } catch (IOException e) {
                            String msg = "I/O error while trying to contact the remote help server";
                            HelpBasePlugin.logError(msg, e);
                        } catch (Throwable t) {
                            String msg = "Internal error while reading search results from remote server";
                            HelpBasePlugin.logError(msg, t);
                        } finally {
                            if (in != null) {
                                try {
                                    in.close();
                                } catch (IOException e) {
                                }
                            }
                        }
                    }
                }
            }
        } finally {
            pm.done();
        }
    }
}
