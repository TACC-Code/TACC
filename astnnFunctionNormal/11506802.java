class BackupThread extends Thread {
    private Element loadDocument(URL url) {
        Document doc = null;
        try {
            InputSource xmlInp = new InputSource(url.openStream());
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder parser = docBuilderFactory.newDocumentBuilder();
            doc = parser.parse(xmlInp);
            Element root = doc.getDocumentElement();
            root.normalize();
            return root;
        } catch (SAXParseException err) {
            log.fatal("CompressionFilterDAO ** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
            log.fatal("CompressionFilterDAO error: " + err.getMessage());
        } catch (SAXException e) {
            log.fatal("CompressionFilterDAO error: " + e);
        } catch (java.net.MalformedURLException mfx) {
            log.fatal("CompressionFilterDAO error: " + mfx);
        } catch (java.io.IOException e) {
            log.fatal("CompressionFilterDAO error: " + e);
        } catch (Exception pce) {
            log.fatal("CompressionFilterDAO error: " + pce);
        }
        return null;
    }
}
