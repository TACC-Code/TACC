class BackupThread extends Thread {
        public void send(MidiMessage mess, long timeStamp) {
            byte[] msgBytes = mess.getMessage();
            if (msgBytes[0] == -1 && msgBytes[1] == 0x51 && msgBytes[2] == 3) {
                int mpq = ((msgBytes[3] & 0xff) << 16) | ((msgBytes[4] & 0xff) << 8) | (msgBytes[5] & 0xff);
                System.out.println("DEBUG RECVIEVED TMEPO " + 60000000f / mpq);
            }
            if (mess instanceof ShortMessage) {
                ShortMessage shm = (ShortMessage) mess;
                switch(shm.getCommand()) {
                    case ShortMessage.CONTROL_CHANGE:
                        System.out.println("ControlChange :" + shm.getData1() + " " + shm.getData2());
                        break;
                    case ShortMessage.PITCH_BEND:
                        short low = (byte) shm.getData1();
                        short high = (byte) shm.getData2();
                        short val = (short) ((high << 7) | low);
                        System.out.println(" Pitch Change = " + high + ":" + low + "  " + val);
                        break;
                    default:
                        int cmd = shm.getCommand();
                        if (cmd != 240) {
                            int chn = shm.getChannel();
                            int dat1 = shm.getData1();
                            int dat2 = shm.getData2();
                            System.out.println(" cmd:" + cmd + " chn:" + chn + " data:" + dat1 + " " + dat2);
                        }
                }
                for (Transmitter t : MidiDebugDevice.this.trans) {
                    if (((Trans) t).recv != null) {
                        ((Trans) t).recv.send(mess, -1);
                    }
                }
            }
        }
}
