class BackupThread extends Thread {
    private List<EPGEntry> fetchData(VdrPersistence vdrP, VdrCache vdrC, VdrUser vu, String time) {
        boolean printUnknwonPattern = true;
        List<EPGEntry> lEPG = new ArrayList<EPGEntry>();
        List<Channel> lC = vdrC.getChannelList();
        VdrConfigShowChannels vcsc = vdrP.fcVdrConfigShowChannels(vu);
        for (Channel c : lC) {
            int chnu = c.getChannelNumber();
            if (vcsc.showChannel(chnu, true)) {
                EPGEntry epg = null;
                boolean unknownPattern = true;
                for (int i = 0; i < pattern.size(); i++) {
                    Matcher m = pattern.get(i).matcher(time);
                    if (m.matches()) {
                        switch(i) {
                            case 0:
                                epg = vdrC.getEpgforTime(c.getChannelNumber(), new Date(), false);
                                break;
                            case 1:
                                epg = vdrC.getEpgforTime(c.getChannelNumber(), new Date(), true);
                                break;
                            case 2:
                                epg = getEpg(m, vdrC, c.getChannelNumber());
                                break;
                        }
                        unknownPattern = false;
                    }
                }
                if (unknownPattern && printUnknwonPattern) {
                    printUnknwonPattern = false;
                    logger.warn("Point in Time " + time + " not in correct format (HH:MM)");
                }
                if (epg != null) {
                    lEPG.add(epg);
                }
            }
        }
        return lEPG;
    }
}
