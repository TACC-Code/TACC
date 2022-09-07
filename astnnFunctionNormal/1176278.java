class BackupThread extends Thread {
    private static void load(final IDOMDocumentBindable browser) {
        try {
            String urlText = ((Textbox) browser.getElementById("url")).getValue();
            URL url = new java.net.URL((new File("./")).toURL(), urlText);
            final DOMParser parser = new org.cyberneko.html.parsers.DOMParser();
            parser.parse(new InputSource(url.openStream()));
            Document domHTMLSelect = parser.getDocument();
            Element element = domHTMLSelect.getDocumentElement();
            Element content = browser.getElementById("content");
            Element contentG = browser.getElementById("contentG");
            if (contentG != null) {
                content.removeChild(contentG);
            }
            contentG = browser.createElementNS(XULConstants.XUL_NAMESPACE_URI, XULConstants.HBOX_ELEMENT);
            contentG.setAttribute("id", "contentG");
            content.appendChild(contentG);
            browser.setBaseURI(urlText);
            Node n = browser.importNode(element, true);
            contentG.appendChild(n);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
