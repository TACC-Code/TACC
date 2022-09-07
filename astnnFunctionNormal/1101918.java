class BackupThread extends Thread {
    public Vector<FluxChannelMap> getLightChannels() {
        return shaderBuilder.getChannelBuilder().getChannels();
    }
}
