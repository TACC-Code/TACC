class BackupThread extends Thread {
    private org.w3c.dom.Document lectureDocumentXML(final URL url) {
        org.w3c.dom.Document ddoc = null;
        try {
            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            final DocumentBuilder docbuilder = dbf.newDocumentBuilder();
            final URLConnection conn = url.openConnection();
            conn.setUseCaches(false);
            ddoc = docbuilder.parse(conn.getInputStream(), url.toExternalForm());
            if (ddoc.getXmlEncoding() != null) encodage = ddoc.getXmlEncoding();
        } catch (final SAXException ex) {
            String infos = rb.getString("erreur.XML") + ":" + newline;
            infos += ex.getMessage();
            if (ex instanceof SAXParseException) infos += " " + rb.getString("erreur.ALaLigne") + " " + ((SAXParseException) ex).getLineNumber();
            JOptionPane.showMessageDialog(jframe, infos, rb.getString("document.Lecture"), JOptionPane.ERROR_MESSAGE);
            return (null);
        } catch (final IOException ex) {
            String infos = rb.getString("erreur.ES") + ":" + newline;
            infos += ex.getMessage();
            JOptionPane.showMessageDialog(jframe, infos, rb.getString("document.Lecture"), JOptionPane.ERROR_MESSAGE);
            return (null);
        } catch (final ParserConfigurationException ex) {
            LOG.error("lire: ParserConfigurationException", ex);
            return (null);
        }
        furl = url;
        try {
            fsave = new File(url.toURI());
        } catch (final Exception ex) {
            fsave = null;
        }
        return (ddoc);
    }
}
