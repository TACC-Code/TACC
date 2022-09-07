class BackupThread extends Thread {
    public String toString() {
        StringBuffer theText = new StringBuffer();
        if (isLinkMessage()) {
            theText.append("Link ");
            theText.append(theLink.toString());
        } else {
            theText.append("Device ");
            theText.append(theDevice.toString());
        }
        switch(theMsgType) {
            case ACTIVATE_LINK:
                theText.append(" ACTIVATED");
                break;
            case DEACTIVATE_LINK:
                theText.append(" DEACTIVATED");
                break;
            case GOTO:
                theText.append(" GOTO level ");
                theText.append(theLevel);
                if (isDeviceMessage()) {
                    if (theDevice.getChannelCount() > 1) {
                        theText.append(" on chan #");
                        theText.append(theChannel);
                    }
                }
                break;
            case REPORT_STATE:
                theText.append(" QUERYING STATE");
                break;
            case FADE_START:
                theText.append(" FADING to level ");
                theText.append(theLevel);
                if (isDeviceMessage()) {
                    if (theDevice.getChannelCount() > 1) {
                        theText.append(" on chan #");
                        theText.append(theChannel);
                    }
                }
                break;
            case FADE_STOP:
                theText.append(" STOPPED FADING");
                break;
            case DEVICE_STATE_RPT:
                theText.append(" STATE REPORT");
                if (isDeviceMessage()) {
                    theText.append(" now @ level ");
                    theText.append(theLevel);
                    if (theDevice.getChannelCount() > 1) {
                        theText.append(" for chan #");
                        theText.append(theChannel);
                    }
                }
                break;
        }
        if (isLinkMessage() && theDevice != null) {
            theText.append(" sent by Device");
            theText.append(theDevice.toString());
        }
        return theText.toString();
    }
}
