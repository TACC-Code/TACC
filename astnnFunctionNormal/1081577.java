class BackupThread extends Thread {
        public void send(MidiMessage mess, long timeStamp) {
            if (mess.getStatus() >= ShortMessage.MIDI_TIME_CODE) {
                return;
            }
            if (mess instanceof ShortMessage) {
                ShortMessage shm = (ShortMessage) mess;
                int cmd = shm.getCommand();
                if (cmd != 240) {
                    int chn = shm.getChannel();
                    int dat1 = shm.getData1();
                    int dat2 = shm.getData2();
                    System.out.println(" cmd:" + cmd + " chn:" + chn + " data:" + dat1 + " " + dat2);
                }
            }
            chained.send(mess, -1);
        }
}
