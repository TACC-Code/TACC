class BackupThread extends Thread {
    private Vector<InputPVTableCell> getChannelVec(Channel p_chan) {
        if (!monitorQueues.containsKey(p_chan.channelName())) {
            monitorQueues.put(p_chan.channelName(), new Vector<InputPVTableCell>());
        }
        return monitorQueues.get(p_chan.channelName());
    }
}
