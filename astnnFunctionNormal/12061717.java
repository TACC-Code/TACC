class BackupThread extends Thread {
    @Override
    public Properties load(String file) throws PropertyLoaderException {
        Properties result = new Properties();
        List<URL> files;
        try {
            files = UtilResources.getFileList(file);
        } catch (IOException e) {
            throw new PropertyLoaderException(e);
        }
        for (URL url : files) {
            if (UtilLog.LOG.isInfoEnabled()) {
                UtilLog.LOG.info("Loading properties:" + url);
            }
            InputStream in = null;
            try {
                in = url.openStream();
                if (in != null) {
                    result.load(in);
                } else {
                    if (UtilLog.LOG.isInfoEnabled()) {
                        UtilLog.LOG.info("Not found:" + url);
                    }
                }
            } catch (IOException e) {
                if (UtilLog.LOG.isDebugEnabled()) {
                    UtilLog.LOG.debug("Not found:" + url, e);
                }
                throw new PropertyLoaderException(e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        if (UtilLog.LOG.isDebugEnabled()) {
                            UtilLog.LOG.debug(e.getMessage(), e);
                        }
                    }
                }
            }
        }
        return result;
    }
}
