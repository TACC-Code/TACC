class BackupThread extends Thread {
    public String getContentsRSS() throws Exception {
        String documentoRSS;
        RSSBuilder doc = new RSSBuilder();
        if (log.isDebugEnabled()) log.debug("RSSBuilder object created");
        doc.setTitle(title);
        if (log.isDebugEnabled()) log.debug("Title and description established");
        int maxItems = Integer.parseInt(maxRSSItems);
        if (log.isDebugEnabled()) log.debug("There are " + contents.size() + " items");
        if (contents.size() < maxItems) {
            maxItems = contents.size();
        }
        for (int i = 0; i < maxItems; i++) {
            if (log.isDebugEnabled()) {
                log.debug("Elemento iterado : " + i);
                log.debug("Longitud contents : " + contents.size());
                log.debug("Contenido i : " + contents.get(i));
            }
            ContentIF item = (ContentIF) contents.get(i);
            doc.addItem(item.getName(), getDescription(item), item.getPublicationDate(), item.getCategories(), this.rssParams.getContentLink(item.getId(), item.getVersion()));
        }
        doc.setType(getRSSVersion());
        ChannelIF canal = doc.getChannelDocument();
        if (log.isDebugEnabled()) {
            log.debug("RSS type of channel is: " + canal.getFormat());
            log.debug("Channel generated");
            log.debug("Exporter generated");
        }
        StringWriter buffer = new StringWriter();
        ChannelExporterIF exporter = doc.getExporter(buffer, "UTF-8");
        if (log.isDebugEnabled()) log.debug("Exporter class is: " + exporter.getClass().getName());
        exporter.write(canal);
        documentoRSS = buffer.toString();
        return documentoRSS;
    }
}
