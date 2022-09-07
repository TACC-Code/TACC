class BackupThread extends Thread {
    public static void setupEventInfo() {
        SimulHandler.setStatus(Settings.SIMUL_OPEN);
        SimulHandler.getHandler().plusNotify(SimulHandler.getGiver().getHandle());
        SimulHandler.getHandler().qchanplus(SimulHandler.getGiver().getHandle(), SimulHandler.getChannel());
        SimulHandler.getHandler().tell(lastTeller, SimulHandler.getGiver().getHandle() + "'s simul is now open for joining.");
        SimulHandler.getHandler().tell(SimulHandler.getGiver().getHandle(), "A new simul has been opened with you as the giver.");
        if (SimulHandler.isNoManager()) {
            SimulHandler.getHandler().tell(SimulHandler.getGiver().getHandle(), "This simul has been setup without a manager. You will have to tell me \"start\" when you are ready to begin the simul.");
            NoManagerTimer.start(Settings.EVENTS_CHANNEL, GIVER_DELAY_TIME);
        }
        if (SimulHandler.getChannelOut()) {
            SimulHandler.getHandler().tell(CHANNEL_3, Common.buildSimulString());
            SimulHandler.getHandler().tell(CHANNEL_4, Common.buildSimulString());
            if (!(SimulHandler.getChannel().equals(CHANNEL_3) || SimulHandler.getChannel().equals(CHANNEL_4))) {
                SimulHandler.getHandler().tell(SimulHandler.getChannel(), Common.buildSimulString());
            }
            Alarm.sendAlarms();
        }
        setupAdvertising();
        if (!SimulHandler.getGiver().isLoggedOn()) {
            SimulHandler.getHandler().tell(lastTeller, "Simul giver \"" + SimulHandler.getGiver().getHandle() + "\" is not currently logged on.");
        } else if (SimulHandler.getGiver().isPlaying()) {
            SimulHandler.getHandler().tell(lastTeller, "Simul giver \"" + SimulHandler.getGiver().getHandle() + "\" is currently played a game.");
        }
    }
}
