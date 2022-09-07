class BackupThread extends Thread {
    public HashMap<String, Double> getDispersionMap() {
        Channels channels = display.getMapPanel().getChannelManager().getChannels();
        HashMap<String, Double> map = new HashMap<String, Double>();
        for (Channel c : channels.getChannels()) {
            map.put(c.getId(), c.getDispersion());
        }
        return map;
    }
}
