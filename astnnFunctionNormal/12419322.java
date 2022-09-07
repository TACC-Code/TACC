class BackupThread extends Thread {
    @SuppressWarnings("unchecked")
    private void _loadProps(String name, String localeKey, boolean useServletContext) {
        Properties props = new Properties();
        try {
            URL url = null;
            if (useServletContext) {
                url = _servletContext.getResource("/WEB-INF/" + name);
            } else {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                url = classLoader.getResource(name);
            }
            if (url != null) {
                InputStream is = url.openStream();
                props.load(is);
                is.close();
                if (_log.isInfoEnabled()) {
                    _log.info("Loading " + url);
                }
            }
        } catch (Exception e) {
            _log.warn(e);
        }
        if (props.size() < 1) {
            return;
        }
        synchronized (messages) {
            Enumeration names = props.keys();
            while (names.hasMoreElements()) {
                String key = (String) names.nextElement();
                messages.put(messageKey(localeKey, key), props.getProperty(key));
            }
        }
    }
}
