class BackupThread extends Thread {
    private String toWordPressFile() throws MySpaceBlogExporterException, InterruptedException {
        String blogString = null;
        try {
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(new URL(getRssLink(getBlogUrl()))));
            if (this.isInterrupted()) {
                throw new InterruptedException();
            }
            ObjectFactory factGen = new ObjectFactory();
            org.wordpress.export._1.ObjectFactory factExp = new org.wordpress.export._1.ObjectFactory();
            Rss rss = factGen.createRss();
            rss.setVersion(new BigDecimal("2.0", new MathContext(2)));
            rss.setChannel(factGen.createRssChannel());
            rss.getChannel().getTitleOrLinkOrDescription().add(factGen.createRssChannelTitle(feed.getTitle()));
            rss.getChannel().getTitleOrLinkOrDescription().add(factGen.createRssChannelLink(feed.getLink()));
            rss.getChannel().getTitleOrLinkOrDescription().add(factGen.createRssChannelDescription(feed.getDescription()));
            rss.getChannel().getTitleOrLinkOrDescription().add(factGen.createRssChannelPubDate(Rfc822Util.getFullDate(feed.getPublishedDate())));
            rss.getChannel().getTitleOrLinkOrDescription().add(factGen.createRssChannelGenerator("MySpaceBlogExporter"));
            rss.getChannel().getTitleOrLinkOrDescription().add(factGen.createRssChannelLanguage(feed.getLanguage()));
            rss.getChannel().getTitleOrLinkOrDescription().add(factExp.createWxrVersion("1.0"));
            Image image = factGen.createImage();
            image.setTitle(feed.getImage().getTitle());
            image.setUrl(feed.getImage().getUrl());
            image.setLink(feed.getImage().getLink());
            rss.getChannel().getTitleOrLinkOrDescription().add(factGen.createRssChannelImage(image));
            String pageUrl = getBlogUrl();
            while (pageUrl != null) {
                if (this.isInterrupted()) {
                    throw new InterruptedException();
                }
                String html = urlToString(pageUrl);
                if (this.isInterrupted()) {
                    throw new InterruptedException();
                }
                rss.getChannel().getItem().addAll(getMSBlogsFromPage(html, factGen, factExp));
                pageUrl = getLinkByTextPattern(html, props.getProperty("olderPostsPattern"));
                addLogMsg("Next page = " + pageUrl);
            }
            addLogMsg("Done adding blogs");
            if (this.isInterrupted()) {
                throw new InterruptedException();
            }
            addLogMsg("Add all categories that we collected...");
            org.wordpress.export._1.Category chanCat = null;
            for (String category : getChanCat()) {
                chanCat = new org.wordpress.export._1.Category();
                chanCat.setCategoryNicename(getNiceName(category));
                chanCat.setCategoryParent("");
                chanCat.setCatName(category);
                rss.getChannel().getTitleOrLinkOrDescription().add(chanCat);
                addLogMsg("...added " + chanCat.getCatName());
            }
            if (this.isInterrupted()) {
                throw new InterruptedException();
            }
            QName rssQname = new QName(props.getProperty("rssLinkText"));
            JAXBElement<Rss> jaxRss = new JAXBElement<Rss>(rssQname, Rss.class, rss);
            addLogMsg("Convert the collected blogs and categories to text");
            blogString = marshalWp(jaxRss);
            jaxRss = null;
            rssQname = null;
            if (this.isInterrupted()) {
                throw new InterruptedException();
            }
            addLogMsg("Fix the blog data blocks. This may take some time; please be patient!");
            blogString = XMLUtil.fixCdata(blogString);
            if (this.isInterrupted()) {
                throw new InterruptedException();
            }
            addLogMsg("Fix the MySpace encoded \"www.msplinks.com\" references. This may take some time; please be patient!");
            blogString = fixMsplinks(blogString);
        } catch (InterruptedException e) {
            throw e;
        } catch (Exception e) {
            MySpaceBlogExporterException e2 = null;
            if (e instanceof MySpaceBlogExporterException) {
                e2 = (MySpaceBlogExporterException) e;
            } else {
                e2 = new MySpaceBlogExporterException(e.getMessage(), e);
            }
            throw e2;
        }
        return blogString;
    }
}
