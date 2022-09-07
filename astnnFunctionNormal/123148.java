class BackupThread extends Thread {
    public int simpleEpgSearch(String lookFor, int type, String cat, String chan, int ignored, int[] times, HashMap<String, Vector<GuideItem>> result) {
        int num = 0;
        GuideItem[] progs = null;
        GuideItem guidItem = null;
        DataStore store = DataStore.getInstance();
        Vector<String[]> channelMap = getChannelMap();
        Set<String> wsChannels = store.getChannels().keySet();
        if (channelMap == null || channelMap.size() == 0) {
            System.out.println("Channel map not set so could not do search.");
            return 0;
        }
        int startHH = 0;
        int startMM = 0;
        int endHH = 23;
        int endMM = 59;
        if (times != null && times.length == 4) {
            startHH = times[0];
            startMM = times[1];
            endHH = times[2];
            endMM = times[3];
        }
        for (int x = 0; x < channelMap.size(); x++) {
            String[] chanMap = (String[]) channelMap.get(x);
            if (wsChannels.contains(chanMap[0])) {
                progs = getProgramsForChannel(chanMap[1]);
                if (progs.length > 0) {
                    Vector<GuideItem> results = new Vector<GuideItem>();
                    for (int y = 0; y < progs.length; y++) {
                        guidItem = progs[y];
                        boolean nameMatches = false;
                        boolean subnameMatches = false;
                        boolean descriptionMatches = false;
                        boolean textMatch = false;
                        boolean catMatches = false;
                        boolean chanMatches = false;
                        boolean ignoreMatches = false;
                        boolean timeSpanMatch = false;
                        if (guidItem.getName().toUpperCase().indexOf(lookFor.toUpperCase()) > -1) nameMatches = true;
                        if (guidItem.getSubName().toUpperCase().indexOf(lookFor.toUpperCase()) > -1) subnameMatches = true;
                        if (guidItem.getDescription().toUpperCase().indexOf(lookFor.toUpperCase()) > -1) descriptionMatches = true;
                        if (type == 0) textMatch = nameMatches | subnameMatches | descriptionMatches; else if (type == 1) textMatch = nameMatches; else if (type == 2) textMatch = subnameMatches; else if (type == 3) textMatch = descriptionMatches;
                        if (cat.equalsIgnoreCase("any")) {
                            catMatches = true;
                        } else {
                            for (int index = 0; index < guidItem.getCategory().size(); index++) {
                                String itemCat = guidItem.getCategory().get(index);
                                if (itemCat.equalsIgnoreCase(cat)) {
                                    catMatches = true;
                                    break;
                                }
                            }
                        }
                        if (chan.equalsIgnoreCase("any")) chanMatches = true;
                        if (chanMap[0].toUpperCase().indexOf(chan.toUpperCase()) > -1) chanMatches = true;
                        if (ignored == 2) ignoreMatches = true; else if (guidItem.getIgnored() == true && ignored == 1) ignoreMatches = true; else if (guidItem.getIgnored() == false && ignored == 0) ignoreMatches = true;
                        if (times != null && times.length == 4) {
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(guidItem.getStart());
                            int startMinInDay = (cal.get(Calendar.HOUR_OF_DAY) * 60) + cal.get(Calendar.MINUTE);
                            int startMinInDaySPAN = (startHH * 60) + startMM;
                            int endMinInDatSPAN = (endHH * 60) + endMM;
                            if (startMinInDaySPAN < endMinInDatSPAN) {
                                if (startMinInDay >= startMinInDaySPAN && startMinInDay <= endMinInDatSPAN) timeSpanMatch = true;
                            } else {
                                if (startMinInDay >= startMinInDaySPAN || startMinInDay <= endMinInDatSPAN) timeSpanMatch = true;
                            }
                        } else {
                            timeSpanMatch = true;
                        }
                        if (catMatches && textMatch && chanMatches && ignoreMatches && timeSpanMatch) {
                            results.add(guidItem);
                            num++;
                        }
                    }
                    if (results.size() > 0) result.put(chanMap[0], results);
                }
            }
        }
        return num;
    }
}
