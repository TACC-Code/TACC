class BackupThread extends Thread {
    public void init(final javax.swing.text.Document doc) {
        String dtdLocation = null;
        XMLLexer lexer = null;
        String schemaLocation = null;
        SyntaxDocument mDoc = (SyntaxDocument) doc;
        if (mDoc.getContentAssist() != null) {
            try {
                String mText = doc.getText(0, doc.getLength());
                Reader mReader = new StringReader(mText);
                lexer = new XMLLexer(mReader);
                parser = new XMLParser(lexer);
                parser.parse();
            } catch (Exception err) {
            }
            if (lexer != null) {
                dtdLocation = lexer.getDTDLocation();
                schemaLocation = lexer.getSchemaLocation();
            }
            try {
                if (dtdLocation != null) {
                    dtdLocation = dtdLocation.substring(1, dtdLocation.length() - 1);
                    XMLAssistProcessor contentAssist = mDoc.getContentAssist();
                    if (contentAssist != null) {
                        contentAssist.setCompletionParser(new net.sf.xpontus.codecompletion.xml.DTDCompletionParser());
                        java.net.URL url = new java.net.URL(dtdLocation);
                        java.io.Reader dtdReader = new java.io.InputStreamReader(url.openStream());
                        contentAssist.updateAssistInfo(lexer.getdtdPublicId(), dtdLocation, dtdReader);
                    }
                } else if (schemaLocation != null) {
                    XMLAssistProcessor contentAssist = mDoc.getContentAssist();
                    if (contentAssist != null) {
                        contentAssist.setCompletionParser(new XMLSchemaCompletionParser());
                        java.net.URL url = new java.net.URL(schemaLocation);
                        java.io.Reader dtdReader = new java.io.InputStreamReader(url.openStream());
                        contentAssist.updateAssistInfo(lexer.getdtdPublicId(), schemaLocation, dtdReader);
                    }
                }
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
    }
}
