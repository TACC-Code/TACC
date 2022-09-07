class BackupThread extends Thread {
    protected final Document toDocument(Source source) throws XMLSignatureException {
        Document doc = null;
        if (source instanceof ObjectSource) {
            try {
                ObjectSource _source = (ObjectSource) source;
                Object _envelope = _source.getMessage();
                XMLStreamReader reader = converter.toXMLStreamReader(_envelope);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                XMLStreamWriter writer = this.getXMLOutputFactory().createXMLStreamWriter(baos);
                StAXUtils.copy(reader, writer);
                writer.flush();
                writer.close();
                return toDocument(new StreamSource(new ByteArrayInputStream(baos.toByteArray())));
            } catch (XMLStreamException e) {
                throw new XMLSignatureException(e);
            } catch (RuntimeException e) {
                if (logger.isErrorEnabled()) {
                    logger.error("Unexpected runtime exception ocurred,processing source :: \n" + source, e);
                }
                throw e;
            }
        } else if (source instanceof StreamSource) {
            try {
                DocumentBuilder db = XmlUtils.newDocumentBuilder();
                doc = db.parse(((StreamSource) source).getInputStream());
            } catch (SAXException e) {
                throw new XMLSignatureException(e);
            } catch (IOException e) {
                throw new XMLSignatureException(e);
            }
        } else if (source instanceof DOMSource) {
            doc = (Document) ((DOMSource) source).getNode();
        } else {
            throw new XMLSignatureException("Not supported Source [" + source + "]");
        }
        return doc;
    }
}
