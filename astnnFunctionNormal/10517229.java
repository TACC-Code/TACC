class BackupThread extends Thread {
    protected void innerProcess(CrawlURI crawlURI) {
        Ugc ugc = (Ugc) crawlURI.getModel();
        String playUrl = ugc.getPlayUrl();
        String videoSite = ugc.getVideoSite();
        String vid = ugc.getVid();
        String evid = ugc.getEvid();
        if (playUrl.endsWith("/")) {
            playUrl = playUrl.substring(0, playUrl.length() - 1);
        }
        CriteriaInfo ci = new CriteriaInfo();
        ci.eq("playUrl", playUrl);
        List<Ugc> ugcList = null;
        if (!canProcess(crawlURI)) {
            if (isInvalidPage(crawlURI)) {
                ugcList = universalBo.getEntitiesByCriteriaInfo(Ugc.class, ci);
                if (ugcList != null && ugcList.size() > 0) {
                    Ugc oldUgc = ugcList.get(0);
                    oldUgc.setStatus(ugc.getStatus());
                    oldUgc.setUpdateDate(new Date());
                    universalBo.doUpdate(oldUgc);
                    log.info(videoSite + "-" + vid + "-无效数据，更新状态。");
                }
            }
            return;
        }
        try {
            ugcList = universalBo.getEntitiesByCriteriaInfo(Ugc.class, ci);
            if (ugcList == null || ugcList.size() == 0) {
                universalBo.doSave(ugc);
                log.info(videoSite + "-" + vid + "-增加数据。");
            } else {
                Ugc oldUgc = ugcList.get(0);
                HashCodeBuilder oldHashCode = new HashCodeBuilder();
                HashCodeBuilder newHashCode = new HashCodeBuilder();
                int length = ugc.getLength();
                if (length > 1) {
                    oldHashCode.append(oldUgc.getLength());
                    newHashCode.append(length);
                    oldUgc.setLength(length);
                }
                String quality = ugc.getQuality();
                if (StringUtils.isNotEmpty(quality)) {
                    oldHashCode.append(oldUgc.getQuality());
                    newHashCode.append(quality);
                    oldUgc.setQuality(quality);
                }
                String channel = ugc.getChannel();
                if (StringUtils.isNotEmpty(channel)) {
                    oldHashCode.append(oldUgc.getChannel());
                    newHashCode.append(channel);
                    oldUgc.setChannel(channel);
                }
                String category = ugc.getCategory();
                if (StringUtils.isNotEmpty(category)) {
                    oldHashCode.append(oldUgc.getCategory());
                    newHashCode.append(category);
                    oldUgc.setCategory(category);
                }
                String tags = ugc.getTags();
                if (StringUtils.isNotEmpty(tags)) {
                    oldHashCode.append(oldUgc.getTags());
                    newHashCode.append(tags);
                    oldUgc.setTags(tags);
                }
                String summary = ugc.getSummary();
                if (StringUtils.isNotEmpty(summary)) {
                    oldHashCode.append(oldUgc.getSummary());
                    newHashCode.append(summary);
                    oldUgc.setSummary(summary);
                }
                if (StringUtils.isNotEmpty(vid)) {
                    oldHashCode.append(oldUgc.getVid());
                    newHashCode.append(vid);
                    oldUgc.setVid(vid);
                }
                if (StringUtils.isNotEmpty(evid)) {
                    oldHashCode.append(oldUgc.getEvid());
                    newHashCode.append(evid);
                    oldUgc.setEvid(evid);
                }
                int totalPageView = ugc.getTotalPageView();
                if (totalPageView > 1) {
                    oldHashCode.append(oldUgc.getTotalPageView());
                    newHashCode.append(totalPageView);
                    oldUgc.setTotalPageView(totalPageView);
                }
                int totalComment = ugc.getTotalComment();
                if (totalComment > 1) {
                    oldHashCode.append(oldUgc.getTotalComment());
                    newHashCode.append(totalComment);
                    oldUgc.setTotalComment(totalComment);
                }
                int totalFav = ugc.getTotalFav();
                if (totalFav > 1) {
                    oldHashCode.append(oldUgc.getTotalFav());
                    newHashCode.append(totalFav);
                    oldUgc.setTotalFav(totalFav);
                }
                String uploadUserid = ugc.getUploadUserid();
                if (StringUtils.isNotEmpty(uploadUserid)) {
                    oldHashCode.append(oldUgc.getUploadUserid());
                    newHashCode.append(uploadUserid);
                    oldUgc.setUploadUserid(uploadUserid);
                }
                String uploadUsername = ugc.getUploadUsername();
                if (StringUtils.isNotEmpty(uploadUsername)) {
                    oldHashCode.append(oldUgc.getUploadUsername());
                    newHashCode.append(uploadUsername);
                    oldUgc.setUploadUsername(uploadUsername);
                }
                String uploadUserblog = ugc.getUploadUserblog();
                if (StringUtils.isNotEmpty(uploadUserblog)) {
                    oldHashCode.append(oldUgc.getUploadUserblog());
                    newHashCode.append(uploadUserblog);
                    oldUgc.setUploadUserblog(uploadUserblog);
                }
                String uploadDate = ugc.getUploadDate();
                if (StringUtils.isNotEmpty(uploadDate)) {
                    oldHashCode.append(oldUgc.getUploadDate());
                    newHashCode.append(uploadDate);
                    oldUgc.setUploadDate(uploadDate);
                }
                long fileSize = ugc.getFileSize();
                if (fileSize > 1l) {
                    oldHashCode.append(oldUgc.getFileSize());
                    newHashCode.append(fileSize);
                    oldUgc.setFileSize(fileSize);
                }
                String fileExt = ugc.getFileExt();
                if (StringUtils.isNotEmpty(fileExt)) {
                    oldHashCode.append(oldUgc.getFileExt());
                    newHashCode.append(fileExt);
                    oldUgc.setFileExt(fileExt);
                }
                oldHashCode.append(oldUgc.getStatus());
                newHashCode.append(ugc.getStatus());
                if (oldHashCode.hashCode() != newHashCode.hashCode()) {
                    oldUgc.setStatus(ugc.getStatus());
                    oldUgc.setCrawlUser(ugc.getCrawlUser());
                    oldUgc.setUpdateUser(ugc.getUpdateUser());
                    oldUgc.setUpdateDate(ugc.getUpdateDate());
                    universalBo.doUpdate(oldUgc);
                    log.info(videoSite + "-" + vid + "-更新数据。");
                }
            }
        } catch (Exception e) {
            log.error(videoSite + "-" + vid + "-DB时发生错误。", e);
        }
    }
}
