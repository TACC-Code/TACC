class BackupThread extends Thread {
    public void setOutDevice(String name) {
        outDevice = deviceManager.getOutDevice(name);
        outputs.clear();
        for (int i = 0; i < outDevice.getChannels() / 2; i++) {
            int chan[] = { i * 2, i * 2 + 1 };
            outputs.add(new StereoOutConnection(outDevice, chan));
        }
    }
}
