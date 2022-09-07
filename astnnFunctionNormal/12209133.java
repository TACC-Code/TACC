class BackupThread extends Thread {
    public int contentStatic(Integer siteId, Integer channelId, Date start, Configuration conf, Map<String, Object> data) throws IOException, TemplateException {
        Finder f = Finder.create("select bean from Content bean");
        if (channelId != null) {
            f.append(" join bean.channel node,Channel parent");
            f.append(" where node.lft between parent.lft and parent.rgt");
            f.append(" and parent.id=:channelId");
            f.append(" and node.site.id=parent.site.id");
            f.setParam("channelId", channelId);
        } else if (siteId != null) {
            f.append(" where bean.site.id=:siteId");
            f.setParam("siteId", siteId);
        } else {
            f.append(" where 1=1");
        }
        if (start != null) {
            f.append(" and bean.sortDate>=:start");
            f.setParam("start", start);
        }
        f.append(" and bean.status=" + ContentCheck.CHECKED);
        Session session = getSession();
        ScrollableResults contents = f.createQuery(session).setCacheMode(CacheMode.IGNORE).scroll(ScrollMode.FORWARD_ONLY);
        int count = 0;
        int totalPage;
        String url, real;
        File file, parent;
        Content c;
        Channel chnl;
        CmsSite site;
        PageInfo info;
        Template tpl;
        Writer out = null;
        if (data == null) {
            data = new HashMap<String, Object>();
        }
        while (contents.next()) {
            c = (Content) contents.get(0);
            chnl = c.getChannel();
            if (!StringUtils.isBlank(c.getLink()) || !chnl.getStaticContent()) {
                continue;
            }
            if (!c.getNeedRegenerate()) {
                continue;
            }
            site = c.getSite();
            tpl = conf.getTemplate(c.getTplContentOrDef());
            FrontUtils.frontData(data, site, null, null, null);
            data.put("content", c);
            data.put("channel", c.getChannel());
            totalPage = c.getPageCount();
            for (int pageNo = 1; pageNo <= totalPage; pageNo++) {
                String txt = c.getTxtByNo(pageNo);
                txt = cmsKeywordMng.attachKeyword(site.getId(), txt);
                Paginable pagination = new SimplePage(pageNo, 1, c.getPageCount());
                data.put("pagination", pagination);
                url = c.getUrlStatic(pageNo);
                info = URLHelper.getPageInfo(url.substring(url.lastIndexOf("/")), null);
                FrontUtils.putLocation(data, url);
                FrontUtils.frontPageData(pageNo, info.getHref(), info.getHrefFormer(), info.getHrefLatter(), data);
                data.put("title", c.getTitleByNo(pageNo));
                data.put("txt", txt);
                data.put("pic", c.getPictureByNo(pageNo));
                if (pageNo == 1) {
                    real = realPathResolver.get(c.getStaticFilename(pageNo));
                    file = new File(real);
                    parent = file.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                } else {
                    real = realPathResolver.get(c.getStaticFilename(pageNo));
                    file = new File(real);
                }
                try {
                    out = new OutputStreamWriter(new FileOutputStream(file), UTF8);
                    tpl.process(data, out);
                    log.info("create static file: {}", file.getAbsolutePath());
                } finally {
                    if (out != null) {
                        out.close();
                    }
                }
            }
            c.setNeedRegenerate(false);
            if (++count % 20 == 0) {
                session.clear();
            }
        }
        return count;
    }
}
