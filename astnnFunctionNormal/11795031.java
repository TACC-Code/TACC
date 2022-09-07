class BackupThread extends Thread {
    public void say(SectionInterface section, String message, ArrayList<String> forceToChannels) {
        SectionSettings sn = null;
        if (section != null) {
            sn = (SectionSettings) _sections.get(section.getName());
        }
        String sc = (sn != null) ? sn.getChannel() : _primaryChannelName;
        if (!forceToChannels.contains(sc.toLowerCase())) forceToChannels.add(sc.toLowerCase());
        for (String chan : forceToChannels) {
            say(chan, message);
        }
    }
}
