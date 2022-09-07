class BackupThread extends Thread {
    public void onMessage(PauseMessage m, List<Message> out) {
        out.add(m);
        if (getChannel().getGameState() == STARTED) {
            stopWatch.suspend();
        }
    }
}
