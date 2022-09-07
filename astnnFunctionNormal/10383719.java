class BackupThread extends Thread {
    @SuppressWarnings("unchecked")
    public List<net.dongliu.jalus.pojo.Feed> getFeedList() throws IOException, IllegalArgumentException {
        String key = "getFeedList";
        if (cache.containsKey(key)) {
            return (List<net.dongliu.jalus.pojo.Feed>) cache.get(key);
        }
        URL url = new URL(feedListUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Cookie", "SID=" + sid);
        connection.setRequestMethod("GET");
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
        SAXBuilder builder = new SAXBuilder(false);
        Document doc;
        try {
            doc = builder.build(reader);
        } catch (JDOMException e1) {
            return null;
        }
        Element root = doc.getRootElement();
        Element listroot = root.getChild("list");
        List<Element> list = listroot.getChildren("object");
        List<net.dongliu.jalus.pojo.Feed> feedList = new ArrayList<net.dongliu.jalus.pojo.Feed>();
        for (Element e : list) {
            List<Element> elist = e.getChildren();
            net.dongliu.jalus.pojo.Feed feed = new net.dongliu.jalus.pojo.Feed();
            for (Element a : elist) {
                if (a.getAttribute("name").getValue().equalsIgnoreCase("id")) {
                    feed.setUri(URLEncoder.encode(a.getValue(), "UTF-8"));
                } else if (a.getAttribute("name").getValue().equalsIgnoreCase("title")) {
                    feed.setTitle(a.getValue());
                } else if (a.getAttribute("name").getValue().equalsIgnoreCase("categories")) {
                } else if (a.getAttribute("name").getValue().equalsIgnoreCase("firstitemmsec")) {
                    Calendar cd = Calendar.getInstance();
                    cd.setTimeInMillis(Long.valueOf(a.getValue()));
                    feed.setPublishDate(cd.getTime());
                }
            }
            feedList.add(feed);
        }
        cache.put(key, feedList);
        return feedList;
    }
}
