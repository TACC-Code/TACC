class BackupThread extends Thread {
        @Override
        public void send(IPatchDriver driver, int value) {
            ShortMessage msg = new ShortMessage();
            try {
                msg.setMessage(ShortMessage.CONTROL_CHANGE, driver.getDevice().getChannel() - 1, param, value * mult);
                driver.send(msg);
            } catch (InvalidMidiDataException e) {
                Logger.reportStatus(e);
            }
        }
}
