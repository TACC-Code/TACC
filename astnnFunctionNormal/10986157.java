class BackupThread extends Thread {
    public void sendDocument(PortableDocument document) {
        if (joinAccepted) {
            LOG.info("--> sendDocument()");
            if (getState() == STATE_ACTIVE) {
                byte[] data = null;
                try {
                    DocumentInfo info = new DocumentInfo(docId, getParticipantId());
                    data = serializer.createResponse(ProtocolConstants.JOIN_DOCUMENT, info, document);
                } catch (SerializeException se) {
                    LOG.error("could not serialize document [" + se.getMessage() + "]");
                }
                Channel outgoing = getChannel();
                setChannel(incoming);
                sendToPeer(data);
                setChannel(outgoing);
            } else {
                LOG.warn("cannot send Document, connection is in state " + getStateString());
            }
            LOG.info("<-- sendDocument()");
        } else {
            throw new IllegalStateException("cannot send document before join is accepted.");
        }
    }
}
