class BackupThread extends Thread {
    public void sendConfigReq(PlayerMcomConfig pmconfig, int whichReq) {
        try {
            int total = 12 + 4 + pmconfig.getChannel_count() + 8 + 4 + pmconfig.getData().getData_count();
            sendHeader(PLAYER_MSGTYPE_REQ, whichReq, total);
            XdrBufferEncodingStream xdr = new XdrBufferEncodingStream(24);
            xdr.beginEncoding(null, 0);
            xdr.xdrEncodeInt(pmconfig.getCommand());
            xdr.xdrEncodeInt(pmconfig.getType());
            xdr.xdrEncodeInt(pmconfig.getChannel_count());
            xdr.xdrEncodeByte((byte) pmconfig.getChannel_count());
            xdr.endEncoding();
            os.write(xdr.getXdrData(), 0, xdr.getXdrLength());
            xdr.close();
            os.flush();
        } catch (Exception e) {
            String subtype = "";
            switch(whichReq) {
                case PLAYER_MCOM_PUSH:
                    {
                        subtype = "PLAYER_MCOM_PUSH";
                        break;
                    }
                case PLAYER_MCOM_POP:
                    {
                        subtype = "PLAYER_MCOM_POP";
                        break;
                    }
                case PLAYER_MCOM_READ:
                    {
                        subtype = "PLAYER_MCOM_READ";
                        break;
                    }
                case PLAYER_MCOM_CLEAR:
                    {
                        subtype = "PLAYER_MCOM_CLEAR";
                        break;
                    }
                case PLAYER_MCOM_SET_CAPACITY:
                    {
                        subtype = "PLAYER_MCOM_SET_CAPACITY";
                        break;
                    }
                default:
                    {
                        logger.log(Level.FINEST, "[MCom] : Couldn't send " + subtype + " command: " + e.toString());
                    }
            }
        }
    }
}
