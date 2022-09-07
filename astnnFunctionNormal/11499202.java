class BackupThread extends Thread {
    public HTMLDocument getHTMLDocument(URL url) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream(), Charset.forName("UTF-8")));
        HTMLDocument htmlDocument = new HTMLDocument(styleSheet);
        try {
            htmlEditorKit.read(bufferedReader, htmlDocument, 0);
        } catch (BadLocationException ble) {
            ble.printStackTrace();
        } finally {
            bufferedReader.close();
        }
        htmlDocument.setBase(url);
        return htmlDocument;
    }
}
