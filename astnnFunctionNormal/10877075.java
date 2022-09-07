class BackupThread extends Thread {
    private void updateSongChannel(TGChannel channel) {
        TGSongManager tgSongManager = TuxGuitar.instance().getSongManager();
        tgSongManager.updateChannel(channel.getChannelId(), channel.getBank(), channel.getProgram(), channel.getVolume(), channel.getBalance(), channel.getChorus(), channel.getReverb(), channel.getPhaser(), channel.getTremolo(), channel.getName());
        TuxGuitar.instance().updateCache(true);
    }
}
