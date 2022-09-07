class BackupThread extends Thread {
    public void transform(String inFilename, String outFilename, Map<String, Object> variables) throws TransformerException {
        OutputStream _outstream = null;
        try {
            XMLReader _reader = makeXMLReader();
            XMLWriter _writer = new VariableXMLHandler(variables);
            _writer.setEncoding(encoding);
            _outstream = new FileOutputStream(new File(outFilename));
            _writer.setOutputStream(_outstream);
            _reader.setContentHandler(_writer.getContentHandler());
            _reader.parse(new org.xml.sax.InputSource(inFilename));
        } catch (Exception e) {
            throw new TransformerException(e.getMessage(), e);
        } finally {
            if (_outstream != null) {
                try {
                    _outstream.flush();
                    _outstream.close();
                } catch (java.io.IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }
}
