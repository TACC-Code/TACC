class BackupThread extends Thread {
    void receiveDeviceGotoCommand(UPBMessage theMessage) {
        updateInternalDeviceLevel(theMessage.getLevel(), theMessage.getFadeRate(), theMessage.getChannel());
    }
}
