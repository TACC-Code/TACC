class BackupThread extends Thread {
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void applyTransform(Element channelNode) {
        if (channelNode == null) throw new NullPointerException();
        try {
            Cache cache;
            Map props = new HashMap();
            props.put(GCacheFactory.EXPIRATION_DELTA, expireSecond);
            CacheFactory cacheFactory;
            cacheFactory = CacheManager.getInstance().getCacheFactory();
            cache = cacheFactory.createCache(props);
            String page = null;
            if (expireSecond > 0) page = (String) cache.get(url);
            if (page == null) {
                HtmlFetcher fetcher = new HtmlFetcher(url);
                page = fetcher.getContent();
                if (expireSecond > 0) cache.put(url, page);
            }
            StringReader sr = new StringReader(page);
            InputSource is = new InputSource(sr);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document rssDocument = db.parse(is);
            NodeList fetchedItems = getChannelNode(rssDocument).getChildNodes();
            for (int i = 0; i < fetchedItems.getLength(); i++) {
                Node item = fetchedItems.item(i);
                if (item.getNodeName().toLowerCase().equals("item")) {
                    channelNode.getOwnerDocument().adoptNode(item);
                    channelNode.appendChild(item);
                }
            }
        } catch (Exception e) {
            System.err.println("���� RSS ʱ�����쳣:");
            System.err.println("��������ҳ��: " + url);
            e.printStackTrace(System.err);
            return;
        }
    }
}
