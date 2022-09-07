class BackupThread extends Thread {
    public void goToAddress(String address) {
        java.net.URL url;
        try {
            url = new java.net.URL(address);
        } catch (java.net.MalformedURLException e) {
            throw new IllegalArgumentException(address + " is not a valid URL", e);
        }
        org.wam.parser.WamParser wamParser = new org.wam.parser.WamDomParser();
        org.wam.core.WamDocument wamDoc;
        try {
            wamDoc = wamParser.parseDocument(new java.io.InputStreamReader(url.openStream()), new org.wam.core.WamDocument.GraphicsGetter() {

                @Override
                public java.awt.Graphics2D getGraphics() {
                    return (java.awt.Graphics2D) WamBrowser.this.getGraphics();
                }
            });
        } catch (java.io.IOException e) {
            throw new IllegalArgumentException("Could not access address " + address, e);
        } catch (org.wam.parser.WamParseException e) {
            throw new IllegalArgumentException("Could not parse XML document at " + address, e);
        }
        wamDoc.postCreate();
        theContentPane.setContent(wamDoc);
        repaint();
    }
}
