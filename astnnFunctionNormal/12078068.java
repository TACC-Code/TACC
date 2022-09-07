class BackupThread extends Thread {
        public void send(MidiMessage message, long timeStamp) {
            if (!isSelected()) return;
            ShortMessage msg = (ShortMessage) message;
            if (msg.getCommand() == ShortMessage.CONTROL_CHANGE) {
                int channel = msg.getChannel();
                int controller = msg.getData1();
                ErrorMsg.reportStatus("FaderReceiver: channel: " + channel + ", control: " + controller + ", value: " + msg.getData2());
                for (int i = 0; i < Constants.NUM_FADERS; i++) {
                    if ((AppConfig.getFaderChannel(i) == channel) && (AppConfig.getFaderControl(i) == controller)) {
                        faderMoved(i, msg.getData2());
                        break;
                    }
                }
            }
        }
}
