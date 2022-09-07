class BackupThread extends Thread {
    public boolean contentStatic(Content c, Configuration conf, Map<String, Object> data) throws IOException, TemplateException {
        Channel chnl = c.getChannel();
        if (!StringUtils.isBlank(c.getLink()) || !chnl.getStaticContent()) {
            return false;
        }
        if (!c.getNeedRegenerate()) {
            return false;
        }
        if (data == null) {
            data = new HashMap<String, Object>();
        }
        int totalPage;
        String url, real;
        File file, parent;
        CmsSite site;
        PageInfo info;
        Template tpl;
        Writer out = null;
        site = c.getSite();
        tpl = conf.getTemplate(c.getTplContentOrDef());
        FrontUtils.frontData(data, site, null, null, null);
        data.put("content", c);
        data.put("channel", chnl);
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
            real = realPathResolver.get(c.getStaticFilename(pageNo));
            file = new File(real);
            if (pageNo == 1) {
                parent = file.getParentFile();
                if (!parent.exists()) {
                    parent.mkdirs();
                }
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
        return true;
    }
}
