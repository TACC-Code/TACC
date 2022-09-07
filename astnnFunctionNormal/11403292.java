class BackupThread extends Thread {
    private Node getChannelNode(Document rssDocument) {
        NodeList items = rssDocument.getDocumentElement().getChildNodes();
        for (int i = 0; i < items.getLength(); i++) {
            Node item = items.item(i);
            if (item.getNodeName().toLowerCase().equals("channel")) return item;
        }
        return null;
    }
}
