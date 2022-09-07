class BackupThread extends Thread {
    private List<TableRow> getDate(String html) throws CardPriceException {
        List<TableRow> rows = new ArrayList<TableRow>();
        try {
            URL url = new URL("http://www.mtgotraders.com/store/" + html);
            Parser parser = new Parser(url.openConnection());
            NodeList list = new NodeList();
            NodeFilter filter = new AndFilter(new TagNameFilter("TR"), new HasChildFilter(new CssSelectorNodeFilter(".products")));
            for (NodeIterator e = parser.elements(); e.hasMoreNodes(); ) {
                e.nextNode().collectInto(list, filter);
            }
            for (Node node : list.toNodeArray()) {
                rows.add((TableRow) node);
            }
            return rows;
        } catch (MalformedURLException e) {
            throw new CardPriceException(e);
        } catch (IOException e) {
            throw new CardPriceException(e);
        } catch (ParserException e) {
            throw new CardPriceException(e);
        }
    }
}
