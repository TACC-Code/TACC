class BackupThread extends Thread {
    private static void queryBioportal() throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        String query = "thymus";
        URL urlQuery = new URL("http://rest.bioontology.org/bioportal/search/" + query + "/?isexactmatch=0" + "&includeproperties=1" + "&maxnumhits=10000000" + "&email=ontocat-svn@lists.sourceforge.net");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(urlQuery.openStream());
        XPathFactory XPfactory = XPathFactory.newInstance();
        XPath xpath = XPfactory.newXPath();
        XPathExpression expr = xpath.compile("//searchResultList/searchBean");
        NodeList terms = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        for (int i = 0; i < terms.getLength(); i++) {
            String ontologyAccession = (String) xpath.evaluate("ontologyId", terms.item(i), XPathConstants.STRING);
            String termAccession = (String) xpath.evaluate("conceptIdShort", terms.item(i), XPathConstants.STRING);
            String label = (String) xpath.evaluate("preferredName", terms.item(i), XPathConstants.STRING);
        }
        log.info(terms.getLength() + " BP terms");
    }
}
