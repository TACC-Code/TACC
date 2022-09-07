class BackupThread extends Thread {
    public void crawl(Baike bk, CrawlURI crawlURI) {
        try {
            fetch.process(crawlURI);
            searchResultPageProcessor.process(crawlURI);
            if (crawlURI.getCrawlStatus() == CrawlStatusCodes.PAGE_PROCESS_SUCCESS) {
                List<CrawlURI> finalCrawlURIList = (List<CrawlURI>) crawlURI.getModel();
                if (finalCrawlURIList.size() > 1) {
                    log.info("匹配数：" + finalCrawlURIList.size());
                }
                CrawlURI finalCrawlURI = finalCrawlURIList.get(0);
                Baike baike = (Baike) finalCrawlURI.getModel();
                doubanFinalPageCrawlJob.crawl(finalCrawlURI);
                if (finalCrawlURI.getCrawlStatus() == CrawlStatusCodes.PAGE_PROCESS_SUCCESS) {
                    String area = baike.getArea();
                    if (StringUtils.isNotEmpty(area)) {
                        area = area.replaceAll("\\s*/\\s*", ",");
                    }
                    String language = baike.getLanguage();
                    if (StringUtils.isNotEmpty(language)) {
                        language = language.replaceAll("\\s*/\\s*", ",");
                    }
                    String channelName = baike.getChannelName();
                    if ("电影".equalsIgnoreCase(channelName)) {
                        baike.setArea(area);
                        baike.setLanguage(language);
                        baike.setId(bk.getId());
                        baike.setChannelCode(bk.getChannelCode());
                        xmlwriter.process(finalCrawlURI);
                        log.info("时光网百科：" + bk.getName() + "-" + bk.getDirectors() + "-" + bk.getStars());
                        log.info("豆瓣百科：" + baike.getName() + "-" + baike.getDirectors() + "-" + baike.getStars());
                    } else {
                        log.info("不是电影");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
