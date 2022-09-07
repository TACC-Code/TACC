class BackupThread extends Thread {
    private void _play(URL url) throws IOException {
        if (conn == null) {
            conn = url.openConnection();
            conn.addRequestProperty("Icy-MetaData", "1");
            conn.connect();
            streamer = new IcecastStreamer(conn);
            streamer.addOnInfoListener(new OnInfoListener() {

                @Override
                public void onInfo(TrackInfos trackInfos) {
                    infos.setTrackInfos(trackInfos);
                    notifyChange();
                }
            });
            player = new AACPlayer();
            Thread playThread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        isPlaying = true;
                        player.play(streamer);
                    } catch (Exception e) {
                        stopWithException(e);
                    }
                }
            });
            playThread.start();
        }
    }
}
