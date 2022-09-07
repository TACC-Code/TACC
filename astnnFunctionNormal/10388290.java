class BackupThread extends Thread {
    private byte[] textInserterPage(HTTPurl urlData) throws Exception {
        StringBuffer buff = new StringBuffer();
        PageTemplate template = new PageTemplate(store.getProperty("path.template") + File.separator + "epg-textInsert.html");
        GuideStore guide = GuideStore.getInstance();
        HashMap<String, Channel> channels = store.getChannels();
        String[] keys = (String[]) channels.keySet().toArray(new String[0]);
        Arrays.sort(keys, String.CASE_INSENSITIVE_ORDER);
        for (int x = 0; x < keys.length; x++) {
            buff.append("<a href='#' onClick=\"addText('" + keys[x] + "');\">" + keys[x] + "</a>");
            if (x < keys.length - 1) buff.append(", ");
        }
        if (buff.length() == 0) buff.append("none");
        template.replaceAll("$channels", buff.toString());
        buff = new StringBuffer();
        String[] cats = guide.getCategoryStrings();
        Arrays.sort(cats, String.CASE_INSENSITIVE_ORDER);
        for (int x = 0; x < cats.length; x++) {
            buff.append("<a href='#' onClick=\"addText('" + cats[x] + "');\">" + cats[x] + "</a>");
            if (x < cats.length - 1) buff.append(", ");
        }
        if (buff.length() == 0) buff.append("none");
        template.replaceAll("$categories", buff.toString());
        buff = new StringBuffer();
        Vector<String> progNames = new Vector<String>();
        HashMap<String, HashMap<String, GuideItem>> progs = guide.getProgramList();
        keys = (String[]) progs.keySet().toArray(new String[0]);
        for (int x = 0; x < keys.length; x++) {
            HashMap<String, GuideItem> channelProgs = (HashMap<String, GuideItem>) progs.get(keys[x]);
            GuideItem[] items = (GuideItem[]) channelProgs.values().toArray(new GuideItem[0]);
            for (int y = 0; y < items.length; y++) {
                if (!progNames.contains(items[y].getName())) progNames.add(items[y].getName());
            }
        }
        String[] names = (String[]) progNames.toArray(new String[0]);
        Arrays.sort(names, String.CASE_INSENSITIVE_ORDER);
        for (int x = 0; x < names.length; x++) {
            String htmlText = HTMLEncoder.encode(names[x]);
            buff.append("<a href='#' onClick=\"addText('" + URLEncoder.encode(names[x], "UTF-8") + "');\">" + htmlText + "</a>");
            if (x < names.length - 1) buff.append(", \n");
        }
        if (buff.length() == 0) buff.append("none");
        template.replaceAll("$programs", buff.toString());
        return template.getPageBytes();
    }
}
