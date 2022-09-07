class BackupThread extends Thread {
    public static void main(String[] args) {
        try {
            String urlStr = "http://blog.csdn.net/yefei679/category/487716.aspx/rss";
            URLConnection feedUrl = new URL(urlStr).openConnection();
            feedUrl.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));
            System.out.println("---------------Begin Output--------------");
            for (Object o : feed.getEntries()) {
                SyndEntry entry = (SyndEntry) o;
                System.out.println("Title: " + entry.getTitle());
                System.out.println("PubDate: " + entry.getPublishedDate());
                System.out.println("Link: " + entry.getUri());
                System.out.println();
            }
            System.out.println("----------------End Output---------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
