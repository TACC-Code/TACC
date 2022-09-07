class BackupThread extends Thread {
    public String decodeMessage(ShortMessage message) {
        String strMessage = null;
        switch(message.getCommand()) {
            case 0x80:
                strMessage = "note Off " + getKeyName(message.getData1()) + " velocity: " + message.getData2();
                break;
            case 0x90:
                strMessage = "note On " + getKeyName(message.getData1()) + " velocity: " + message.getData2();
                break;
            case 0xa0:
                strMessage = "polyphonic key pressure " + getKeyName(message.getData1()) + " pressure: " + message.getData2();
                break;
            case 0xb0:
                strMessage = "control change " + message.getData1() + " value: " + message.getData2();
                break;
            case 0xc0:
                strMessage = "program change " + message.getData1();
                break;
            case 0xd0:
                strMessage = "key pressure " + getKeyName(message.getData1()) + " pressure: " + message.getData2();
                break;
            case 0xe0:
                strMessage = "pitch wheel change " + get14bitValue(message.getData1(), message.getData2());
                break;
            case 0xF0:
                strMessage = SYSTEM_MESSAGE_TEXT[message.getChannel()];
                switch(message.getChannel()) {
                    case 0x1:
                        int nQType = (message.getData1() & 0x70) >> 4;
                        int nQData = message.getData1() & 0x0F;
                        if (nQType == 7) {
                            nQData = nQData & 0x1;
                        }
                        strMessage += QUARTER_FRAME_MESSAGE_TEXT[nQType] + nQData;
                        if (nQType == 7) {
                            int nFrameType = (message.getData1() & 0x06) >> 1;
                            strMessage += ", frame type: " + FRAME_TYPE_TEXT[nFrameType];
                        }
                        break;
                    case 0x2:
                        strMessage += get14bitValue(message.getData1(), message.getData2());
                        break;
                    case 0x3:
                        strMessage += message.getData1();
                        break;
                }
                break;
            default:
                strMessage = "unknown message: status = " + message.getStatus() + ", byte1 = " + message.getData1() + ", byte2 = " + message.getData2();
                break;
        }
        if (message.getCommand() != 0xF0) {
            int nChannel = message.getChannel() + 1;
            String strChannel = "channel " + nChannel + ": ";
            strMessage = strChannel + strMessage;
        }
        smCount++;
        return "[" + getHexString(message) + "] " + strMessage;
    }
}
