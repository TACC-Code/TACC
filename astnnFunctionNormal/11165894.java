class BackupThread extends Thread {
    public HTMLDocument loadDocument(HTMLDocument doc, URL url, String charSet) throws IOException {
        doc.putProperty(Document.StreamDescriptionProperty, url);
        InputStream in = null;
        boolean ignoreCharSet = false;
        for (; ; ) {
            try {
                doc.remove(0, doc.getLength());
                URLConnection urlc = url.openConnection();
                in = urlc.getInputStream();
                Reader reader = (charSet == null) ? new InputStreamReader(in) : new InputStreamReader(in, charSet);
                HTMLEditorKit.Parser parser = getParser();
                HTMLEditorKit.ParserCallback htmlReader = getParserCallback(doc);
                parser.parse(reader, htmlReader, ignoreCharSet);
                htmlReader.flush();
                break;
            } catch (BadLocationException ex) {
                throw new IOException(ex.getMessage());
            } catch (ChangedCharSetException e) {
                charSet = getNewCharSet(e);
                ignoreCharSet = true;
                in.close();
            }
        }
        return doc;
    }
}
