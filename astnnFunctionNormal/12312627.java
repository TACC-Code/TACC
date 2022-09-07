class BackupThread extends Thread {
    public boolean process(ManagerEvent event) {
        if (getState() == Call.ACTIVE_STATE) {
            if (event instanceof MeetMeLeaveEvent) {
                MeetMeLeaveEvent mmle = (MeetMeLeaveEvent) event;
                logger.info("MeetMeLeaveEvent[" + mmle.getChannel() + "]");
                logger.info("channels " + channels.size());
                for (Channel.Descriptor channelDesc : channels.keySet()) {
                    if (channelDesc.getId().equals(mmle.getChannel())) {
                        removeChannel(channelDesc);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
