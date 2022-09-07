class BackupThread extends Thread {
        public void run() {
            RssFeedLoader loader = new RssFeedLoader();
            int index;
            int channelCount = SettingsData.getChannelCount();
            Rss[] feedsArray = new Rss[channelCount];
            final Vector<Rss> feeds = new Vector<Rss>();
            for (index = 1; index <= channelCount; index++) {
                String channelUrl = SettingsData.getChannelUrl(index);
                RssFeedHandlerObserver feedObserver = new RssFeedHandlerObserver() {

                    public void startDocument() {
                    }

                    public void endDocument(Rss rootElement) {
                        feeds.add(rootElement);
                    }

                    public void warning(SAXParseException exception) {
                    }

                    public void error(SAXParseException exception) {
                    }

                    public void error(MalformedFeedElementException exception) {
                    }

                    public void fatalError(SAXParseException exception) {
                    }
                };
                SyndicationErrorObserver errorObserver = new SyndicationErrorObserver() {

                    public void error(MalformedURLException exception) {
                    }

                    public void error(SAXException exception) {
                    }

                    public void error(IOException exception) {
                    }
                };
                loader.startLoadingSync(channelUrl, feedObserver, errorObserver);
            }
            feedsArray = feeds.toArray(feedsArray);
            ChannelList chanList = new ChannelList(feedsArray);
            if (model != null) {
                model.setChannelList(chanList, isFirstTimeLoad);
            }
            busyIconTimer.stop();
            statusAnimationLabel.setIcon(idleIcon);
            statusMessageLabel.setText("");
        }
}
