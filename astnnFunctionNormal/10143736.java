class BackupThread extends Thread {
    @Override
    public void sendResponse(IResponse response) {
        assert (response != null);
        assert (this.simulationClient != null);
        StringWriter writer = new StringWriter();
        ResponseXMLHandler.writeResponse(response, writer);
        StringReader reader = new StringReader(writer.toString());
        this.simulationClient.receiveRequest(reader);
    }
}
