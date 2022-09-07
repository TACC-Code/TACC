class BackupThread extends Thread {
    public static int getDeviceId(MidiMessage msg) {
        return getChannel(msg);
    }
}
