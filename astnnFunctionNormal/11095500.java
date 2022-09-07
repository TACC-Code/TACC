class BackupThread extends Thread {
    public void setInDevice(String name) {
        inDevice = deviceManager.getInDevice(name);
        inputs.clear();
        for (int i = 0; i < inDevice.getChannels(); i++) {
            inputs.add(new MonoInConnection(inDevice, i));
        }
        for (int i = 0; i < inDevice.getChannels() / 2; i++) {
            int chan[] = { i * 2, i * 2 + 1 };
            inputs.add(new StereoInConnection(inDevice, chan));
        }
    }
}
