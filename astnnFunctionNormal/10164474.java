class BackupThread extends Thread {
    public final void interceptMessage(MidiMessage msg, Receiver receiver) {
        try {
            ShortMessage shm = (ShortMessage) msg;
            if (shm.getCommand() == ShortMessage.NOTE_ON) {
                if (shm.getData2() == 0) {
                    pendingNoteOffs.get(receiver).remove(shm.getChannel() << 8 | shm.getData1());
                } else {
                    if (!pendingNoteOffs.containsKey(receiver)) pendingNoteOffs.put(receiver, new HashSet<Integer>());
                    pendingNoteOffs.get(receiver).add(shm.getChannel() << 8 | shm.getData1());
                }
            }
        } catch (Exception e) {
        }
    }
}
