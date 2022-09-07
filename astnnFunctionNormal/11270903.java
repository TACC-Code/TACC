class BackupThread extends Thread {
    public void receiveMessage(UPBMessage theMessage) {
        switch(theMessage.getMsgType()) {
            case GOTO:
            case FADE_START:
                setAllDeviceLevels(theMessage.getLevel(), theMessage.getFadeRate(), theMessage.getChannel());
                break;
            case FADE_STOP:
                for (UPBDeviceI theDevice : deviceNetwork.getDevices()) {
                    if (theDevice == null) continue;
                    deviceNetwork.getUPBManager().queueStateRequest(theDevice);
                }
                break;
        }
    }
}
