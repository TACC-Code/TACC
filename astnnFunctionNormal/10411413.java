class BackupThread extends Thread {
    public static String getErrorRSSDocument(Exception error) throws Exception {
        String documentoRSS;
        RSSBuilder doc = new RSSBuilder();
        String errorName = error.getClass().getName();
        if (errorName.equals(ContentNotFoundException.class.getName())) {
            doc.setTitle(CmsToolsConfig.getRSSErrorTitle(errorName));
            doc.setDescription(CmsToolsConfig.getRSSErrorMessage(errorName));
        } else if (errorName.equals(NotAuthorizedException.class.getName())) {
            doc.setTitle(CmsToolsConfig.getRSSErrorTitle(errorName));
            doc.setDescription(CmsToolsConfig.getRSSErrorMessage(errorName));
        } else {
            doc.setTitle(CmsToolsConfig.getRSSErrorTitle(errorName));
            doc.setDescription(CmsToolsConfig.getRSSErrorMessage(errorName));
        }
        doc.setType(getRSSVersion());
        ChannelIF canal = doc.getChannelDocument();
        if (log.isDebugEnabled()) {
            log.debug("Channel generated");
            log.debug("Exporter generated");
        }
        StringWriter buffer = new StringWriter();
        ChannelExporterIF exporter = doc.getExporter(buffer, "iso-8859-1");
        exporter.write(canal);
        documentoRSS = buffer.toString();
        return documentoRSS;
    }
}
