class BackupThread extends Thread {
    public WanRenderable createEpgForTime(HttpServletRequest request) {
        VdrPersistence vdrP = (VdrPersistence) getServletContext().getAttribute(VdrPersistence.class.getSimpleName());
        VdrCache vdrC = (VdrCache) getServletContext().getAttribute(VdrCache.class.getSimpleName());
        VdrUser vu = (VdrUser) request.getSession().getAttribute(VdrUser.class.getSimpleName());
        ServletForm form = new ServletForm(request);
        List<EPGEntry> lEPG = fetchData(vdrP, vdrC, vu, form.get("time"));
        WanMenu wm = new WanMenu();
        wm.setMenuType(WanMenu.MenuType.IMAGE);
        logger.trace("WanMenu (" + wm.getMenuType() + ") created, population with " + lEPG.size() + " entries");
        for (EPGEntry e : lEPG) {
            WanMenuEntry wmi = new WanMenuEntry();
            wmi.setName(e.getTitle());
            HtmlHref href = new HtmlHref();
            if (form.get("time").equals("NOW")) {
                href.setTargetLink("async/switch");
                href.addHtPa("chid", e.getChannelID());
            } else {
                href = lyrEpgDetail.getLayerTarget();
                href.addHtPa("chNu", vdrC.getChNum(e.getChannelID()));
                href.addHtPa("st", e.getStartTime().getTimeInMillis());
            }
            href.setRev(HtmlHref.Rev.async);
            wmi.setHtmlref(href);
            wmi.setFooter(e.getChannelName());
            wmi.setHeader(DateUtil.sm(e.getStartTime()) + " - " + DateUtil.sm(e.getEndTime()));
            wm.addItem(wmi);
        }
        return wm;
    }
}
