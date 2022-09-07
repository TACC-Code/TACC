class BackupThread extends Thread {
    public RssFeeds() {
        try {
            ejb.bprocess.administration.TopStoriesRSSFeedSession topSession = ((ejb.bprocess.administration.TopStoriesRSSFeedSessionHome) ejb.bprocess.util.HomeFactory.getInstance().getRemoteHome("TopStoriesRSSFeedSession")).create();
            for (int i = 0; i < vec.size(); i++) {
                java.net.URL url = new java.net.URL("" + vec.elementAt(i).toString() + "");
                java.net.URLConnection urlc = (java.net.URLConnection) url.openConnection();
                urlc.setDoOutput(true);
                java.io.InputStream is = urlc.getInputStream();
                org.jdom.input.SAXBuilder sb = new org.jdom.input.SAXBuilder();
                org.jdom.Document doc = sb.build(is);
                java.util.List item = doc.getRootElement().getChild("channel").getChildren("item");
                for (int j = 0; j < item.size(); j++) {
                    org.jdom.Element items = (org.jdom.Element) item.get(j);
                    java.util.List itemchildren = items.getChildren();
                    for (int k = 0; k < itemchildren.size(); k++) {
                        org.jdom.Element child = (org.jdom.Element) item.get(k);
                        vec1.addElement(child.getText());
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
