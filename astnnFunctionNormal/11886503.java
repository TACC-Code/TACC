class BackupThread extends Thread {
    void handleService(KNXnetIPHeader h, byte[] data, int offset) throws KNXFormatException, IOException {
        final int svc = h.getServiceType();
        if (svc == KNXnetIPHeader.TUNNELING_REQ) {
            ServiceRequest req;
            try {
                req = PacketHelper.getServiceRequest(h, data, offset);
            } catch (final KNXFormatException e) {
                req = PacketHelper.getEmptyServiceRequest(h, data, offset);
                final byte[] junk = new byte[h.getTotalLength() - h.getStructLength() - 4];
                System.arraycopy(data, offset + 4, junk, 0, junk.length);
                logger.warn("received tunneling request with unknown cEMI part " + DataUnitBuilder.toHex(junk, " "), e);
            }
            if (req.getChannelID() != getChannelID()) {
                logger.warn("received wrong request channel-ID " + req.getChannelID() + ", expected " + getChannelID() + " - ignored");
                return;
            }
            final short seq = req.getSequenceNumber();
            if (seq == getSeqNoRcv() || seq + 1 == getSeqNoRcv()) {
                final short status = h.getVersion() == KNXNETIP_VERSION_10 ? ErrorCodes.NO_ERROR : ErrorCodes.VERSION_NOT_SUPPORTED;
                final byte[] buf = PacketHelper.toPacket(new ServiceAck(KNXnetIPHeader.TUNNELING_ACK, getChannelID(), seq, status));
                final DatagramPacket p = new DatagramPacket(buf, buf.length, dataEP.getAddress(), dataEP.getPort());
                socket.send(p);
                if (status == ErrorCodes.VERSION_NOT_SUPPORTED) {
                    close(ConnectionCloseEvent.INTERNAL, "protocol version changed", LogLevel.ERROR, null);
                    return;
                }
            } else logger.warn("tunneling request invalid receive-sequence " + seq + ", expected " + getSeqNoRcv());
            if (seq == getSeqNoRcv()) {
                incSeqNoRcv();
                final CEMI cemi = req.getCEMI();
                if (cemi == null) return;
                if (cemi.getMessageCode() == CEMILData.MC_LDATA_IND || cemi.getMessageCode() == CEMIBusMon.MC_BUSMON_IND) fireFrameReceived(cemi); else if (cemi.getMessageCode() == CEMILData.MC_LDATA_CON) {
                    fireFrameReceived(cemi);
                    setStateNotify(OK);
                } else if (cemi.getMessageCode() == CEMILData.MC_LDATA_REQ) logger.warn("received L-Data request - ignored");
            }
        } else logger.warn("received unknown frame (service type 0x" + Integer.toHexString(svc) + ") - ignored");
    }
}
