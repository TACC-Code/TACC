class BackupThread extends Thread {
    public void setDeviceInfo(UPBNetworkI theNetwork, UPBProductI theProduct, int deviceID) {
        this.deviceNetwork = theNetwork;
        this.upbProduct = theProduct;
        this.deviceID = deviceID;
        channelCount = upbProduct.getChannelCount();
        primaryChannel = upbProduct.getPrimaryChannel();
        receiveComponentCount = upbProduct.getReceiveComponentCount();
        deviceState = new int[(getChannelCount() < 1) ? 1 : getChannelCount()];
        isDimmable = new boolean[(getChannelCount() < 1) ? 1 : getChannelCount()];
        for (int chanIndex = 0; chanIndex < deviceState.length; chanIndex++) {
            deviceState[chanIndex] = UNASSIGNED_DEVICE_STATE;
            isDimmable[chanIndex] = upbProduct.isDimmingCapable();
        }
    }
}
