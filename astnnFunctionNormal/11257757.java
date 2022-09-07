class BackupThread extends Thread {
    public void onMessage(ResumeMessage m, List<Message> out) {
        out.add(m);
        if (getChannel().getGameState() == PAUSED) {
            stopWatch.resume();
        }
    }
}
