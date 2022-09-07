class BackupThread extends Thread {
    public void onMessage(StartGameMessage m, List<Message> out) {
        out.add(m);
        if (getChannel().getGameState() == STOPPED) {
            stopWatch.reset();
            stopWatch.start();
        }
        Settings settings = getChannel().getConfig().getSettings();
        int time = settings.getSuddenDeathTime() * 1000;
        if (time > 0) {
            timer = new Timer();
            timer.schedule(new Task(time), max(0, time - Task.WARNING_DELAY * 1000), 200);
        }
    }
}
