class BackupThread extends Thread {
        InputStream getResourceAsStream(String path, boolean locate) throws IOException, ServletException {
            if (locate) path = Servlets.locate(_webctx.getServletContext(), this.request, path, _webctx.getLocator());
            if (_cache.getCheckPeriod() >= 0) {
                try {
                    URL url = _webctx.getResource(path);
                    if (url != null) return url.openStream();
                } catch (Throwable ex) {
                    log.warningBriefly("Unable to read from URL: " + path, ex);
                }
            }
            return _webctx.getResourceAsStream(path);
        }
}
