class BackupThread extends Thread {
    public IStatus addDocument(ISearchIndex index, String pluginId, String name, URL url, String id, Document doc) {
        InputStream stream = null;
        try {
            if (parser == null) {
                parser = SAXParserFactory.newInstance().newSAXParser();
            }
            stack.clear();
            hasFilters = false;
            ParsedXMLContent parsed = new ParsedXMLContent(index.getLocale());
            XMLHandler handler = new XMLHandler(parsed);
            stream = url.openStream();
            stream = preprocess(stream, name, index.getLocale());
            parser.parse(stream, handler);
            doc.add(new Field("contents", parsed.newContentReader()));
            doc.add(new Field("exact_contents", parsed.newContentReader()));
            String title = parsed.getTitle();
            if (title != null) addTitle(title, doc);
            String summary = parsed.getSummary();
            if (summary != null) doc.add(new Field("summary", summary, Field.Store.YES, Field.Index.NO));
            if (hasFilters) {
                doc.add(new Field("filters", "true", Field.Store.YES, Field.Index.NO));
            }
            return Status.OK_STATUS;
        } catch (Exception e) {
            return new Status(IStatus.ERROR, HelpBasePlugin.PLUGIN_ID, IStatus.ERROR, "Exception occurred while adding document " + name + " to index.", e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                }
                stream = null;
            }
        }
    }
}
