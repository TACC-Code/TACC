class BackupThread extends Thread {
    public void onMessage(EndGameMessage m, List<Message> out) {
        out.add(m);
        if (getChannel().getGameState() != STOPPED) {
            stopWatch.stop();
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
