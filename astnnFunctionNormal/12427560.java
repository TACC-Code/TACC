class BackupThread extends Thread {
    public int searchEPG(EpgMatchList epgMatchList, HashMap<String, Vector<GuideItem>> result) {
        DataStore store = DataStore.getInstance();
        int num = 0;
        GuideItem[] progs = null;
        Vector<String[]> channelMap = getChannelMap();
        if (channelMap == null || channelMap.size() == 0) {
            return 0;
        }
        Set<String> wsChannels = store.getChannels().keySet();
        for (int x = 0; x < channelMap.size(); x++) {
            String[] map = (String[]) channelMap.get(x);
            if (wsChannels.contains(map[0])) {
                progs = getProgramsForChannel(map[1]);
                if (progs.length > 0) {
                    Vector<GuideItem> results = null;
                    if (result.containsKey(map[0])) {
                        results = (Vector<GuideItem>) result.get(map[0]);
                    } else {
                        results = new Vector<GuideItem>();
                        result.put(map[0], results);
                    }
                    for (int y = 0; y < progs.length; y++) {
                        if (epgMatchList.isMatch(progs[y], map[0])) {
                            if (!results.contains(progs[y])) {
                                results.add(progs[y]);
                                num++;
                            }
                        }
                    }
                }
            }
        }
        return num;
    }
}
