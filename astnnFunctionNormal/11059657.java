class BackupThread extends Thread {
    public Parse getParse(Content content) {
        List theRSSChannels = null;
        try {
            byte[] raw = content.getContent();
            FeedParser parser = FeedParserFactory.newFeedParser();
            FeedParserListener listener = new FeedParserListenerImpl();
            parser.parse(listener, new ByteArrayInputStream(raw), null);
            theRSSChannels = ((FeedParserListenerImpl) listener).getChannels();
        } catch (Exception e) {
            if (LOG.isWarnEnabled()) {
                e.printStackTrace(LogUtil.getWarnStream(LOG));
                LOG.warn("nutch:parse-rss:RSSParser Exception: " + e.getMessage());
            }
            return new ParseStatus(ParseStatus.FAILED, "Can't be handled as rss document. " + e).getEmptyParse(getConf());
        }
        StringBuffer contentTitle = new StringBuffer(), indexText = new StringBuffer();
        List theOutlinks = new Vector();
        if (theRSSChannels != null) {
            for (int i = 0; i < theRSSChannels.size(); i++) {
                RSSChannel r = (RSSChannel) theRSSChannels.get(i);
                contentTitle.append(r.getTitle());
                contentTitle.append(" ");
                indexText.append(r.getDescription());
                indexText.append(" ");
                if (r.getLink() != null) {
                    try {
                        if (r.getDescription() != null) {
                            theOutlinks.add(new Outlink(r.getLink(), r.getDescription(), getConf()));
                        } else {
                            theOutlinks.add(new Outlink(r.getLink(), "", getConf()));
                        }
                    } catch (MalformedURLException e) {
                        if (LOG.isWarnEnabled()) {
                            LOG.warn("MalformedURL: " + r.getLink());
                            LOG.warn("Attempting to continue processing outlinks");
                            e.printStackTrace(LogUtil.getWarnStream(LOG));
                        }
                        continue;
                    }
                }
                for (int j = 0; j < r.getItems().size(); j++) {
                    RSSItem theRSSItem = (RSSItem) r.getItems().get(j);
                    indexText.append(theRSSItem.getDescription());
                    indexText.append(" ");
                    String whichLink = null;
                    if (theRSSItem.getPermalink() != null) whichLink = theRSSItem.getPermalink(); else whichLink = theRSSItem.getLink();
                    if (whichLink != null) {
                        try {
                            if (theRSSItem.getDescription() != null) {
                                theOutlinks.add(new Outlink(whichLink, theRSSItem.getDescription(), getConf()));
                            } else {
                                theOutlinks.add(new Outlink(whichLink, "", getConf()));
                            }
                        } catch (MalformedURLException e) {
                            if (LOG.isWarnEnabled()) {
                                LOG.warn("MalformedURL: " + whichLink);
                                LOG.warn("Attempting to continue processing outlinks");
                                e.printStackTrace(LogUtil.getWarnStream(LOG));
                            }
                            continue;
                        }
                    }
                }
            }
            if (LOG.isTraceEnabled()) {
                LOG.trace("nutch:parse-rss:getParse:indexText=" + indexText);
                LOG.trace("nutch:parse-rss:getParse:contentTitle=" + contentTitle);
            }
        } else if (LOG.isTraceEnabled()) {
            LOG.trace("nutch:parse-rss:Error:getParse: No RSS Channels recorded!");
        }
        Outlink[] outlinks = (Outlink[]) theOutlinks.toArray(new Outlink[theOutlinks.size()]);
        if (LOG.isTraceEnabled()) {
            LOG.trace("nutch:parse-rss:getParse:found " + outlinks.length + " outlinks");
        }
        ParseData parseData = new ParseData(ParseStatus.STATUS_SUCCESS, contentTitle.toString(), outlinks, content.getMetadata());
        parseData.setConf(this.conf);
        return new ParseImpl(indexText.toString(), parseData);
    }
}
