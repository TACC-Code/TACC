class BackupThread extends Thread {
    private Vector getProgrammes() {
        channelLabels = new Vector();
        Vector channels = null;
        Programme nowProg = null;
        Programme nextProg = null;
        channels = model.getChannels();
        Calendar tm = Calendar.getInstance();
        for (Iterator i = channels.iterator(); i.hasNext(); ) {
            Channel ch = (Channel) i.next();
            if (ch.isActive()) {
                nowProg = ch.getByTime(tm);
                nextProg = ch.getNext(tm, 1);
                if (nowProg != null || !model.isHidingNotOnAir()) {
                    String channelName = (ch != null ? ch.getName() : "unknown");
                    String channelAlias = (ch != null ? ch.getAlias() : "unknown");
                    String nowDesc = (nowProg != null ? nowProg.getShortHTML() : "Not on Air");
                    String nextDesc = (nextProg != null ? nextProg.getShortHTML() : "Not on Air");
                    StringBuffer html = new StringBuffer("<html");
                    html.append("<font face=\"Trebuchet MS\" size=\"2\"><b>");
                    html.append(channelAlias);
                    html.append("</b><br>");
                    html.append(nowDesc);
                    html.append("<br>");
                    html.append(nextDesc);
                    html.append("</font></html>");
                    NowAndNextLabel progLab = new NowAndNextLabel(new String(html));
                    progLab.setName("channel:" + channelName);
                    progLab.setAnchor((nowProg != null ? nowProg.getStart() : "top"));
                    channelLabels.add(progLab);
                }
            }
        }
        return channelLabels;
    }
}
