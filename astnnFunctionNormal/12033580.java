class BackupThread extends Thread {
    public void initializeIt(String path) {
        nameList = new ArrayList<String>();
        typeList = new ArrayList<String>();
        try {
            url = new URL(path);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HTMLEditorKit kit = new HTMLEditorKit();
        HTMLDocument doc = (HTMLDocument) kit.createDefaultDocument();
        doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);
        Reader HTMLReader;
        try {
            HTMLReader = new InputStreamReader(url.openConnection().getInputStream());
            kit.read(HTMLReader, doc, 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        ElementIterator it = new ElementIterator(doc);
        Element elem;
        while ((elem = it.next()) != null) {
            String elemanismi = elem.getName();
            String elemanDegeri = (String) elem.getAttributes().getAttribute(HTML.Attribute.TYPE);
            nameList.add(elemanismi);
            typeList.add(elemanDegeri);
        }
    }
}
