class BackupThread extends Thread {
    @SuppressWarnings("unchecked")
    public static List<Element> getTwitter(String username) throws IOException, JDOMException {
        URL url = new URL("http://phill84.org/twitter.php?u=" + username);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        SAXBuilder builder = new SAXBuilder();
        Document twitFeed = builder.build(reader);
        Element root = twitFeed.getRootElement();
        List<Element> status = root.getChildren();
        return status;
    }
}
