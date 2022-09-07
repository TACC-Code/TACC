class BackupThread extends Thread {
    public void eliminateDoubles() {
        int doubles = 0;
        for (int i = 0; i < changes.size() - 1; i++) {
            int channelId = changes.get(i).getChannelId();
            for (int j = i + 1; j < changes.size(); j++) {
                if (channelId == changes.get(j).getChannelId()) {
                    changes.set(i, null);
                    doubles++;
                    break;
                }
            }
        }
        if (doubles > 0) {
            Iterator<ChannelChange> i = changes.iterator();
            while (i.hasNext()) {
                if (i.next() == null) {
                    i.remove();
                }
            }
        }
    }
}
