class BackupThread extends Thread {
    private TGChannel[] makeChannels(TGSong song) {
        TGChannel[] channels = new TGChannel[64];
        for (int i = 0; i < channels.length; i++) {
            channels[i] = getFactory().newChannel();
            channels[i].setProgram((short) 24);
            channels[i].setVolume((short) 13);
            channels[i].setBalance((short) 8);
            channels[i].setChorus((short) 0);
            channels[i].setReverb((short) 0);
            channels[i].setPhaser((short) 0);
            channels[i].setTremolo((short) 0);
        }
        Iterator it = song.getChannels();
        while (it.hasNext()) {
            TGChannel tgChannel = (TGChannel) it.next();
            GMChannelRoute gmChannelRoute = getChannelRoute(tgChannel.getChannelId());
            channels[gmChannelRoute.getChannel1()].setProgram(tgChannel.getProgram());
            channels[gmChannelRoute.getChannel1()].setVolume(tgChannel.getVolume());
            channels[gmChannelRoute.getChannel1()].setBalance(tgChannel.getBalance());
            channels[gmChannelRoute.getChannel2()].setProgram(tgChannel.getProgram());
            channels[gmChannelRoute.getChannel2()].setVolume(tgChannel.getVolume());
            channels[gmChannelRoute.getChannel2()].setBalance(tgChannel.getBalance());
        }
        return channels;
    }
}
