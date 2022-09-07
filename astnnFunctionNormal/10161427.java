class BackupThread extends Thread {
    public void generateStatistics() {
        for (Track track : getTracks()) {
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage shortMessage = (ShortMessage) message;
                    if (shortMessage.getChannel() == PERCUSSION_CHANNEL) break;
                    parseShortMessage(shortMessage);
                } else if (message instanceof MetaMessage) {
                    MetaMessage metaMessage = (MetaMessage) message;
                    parseMetaMessage(metaMessage);
                } else if (message instanceof SysexMessage) {
                    SysexMessage sysexMessage = (SysexMessage) message;
                    parseSysexMessage(sysexMessage);
                }
            }
        }
        if (keySignature == null) keySignature = KeySignature.C;
        calculateRatios();
        statisticsAvailable = true;
    }
}
