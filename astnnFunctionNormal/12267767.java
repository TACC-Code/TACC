class BackupThread extends Thread {
    private void executeAt(Reader reader, Writer writer, String remoteEndpoint) throws XQueryException, IOException {
        XQEngine engine = new XQEngineClient(remoteEndpoint);
        String query = IOUtils.toString(reader);
        QueryRequest request = new QueryRequest(query, ReturnType.ASYNC_REMOTE_SEQUENCE);
        final Sequence<Item> resultSeq;
        try {
            resultSeq = (Sequence<Item>) engine.execute(request);
        } catch (RemoteException e) {
            throw new XQueryException("failed to execute a query", e);
        }
        SAXWriter saxwr = prepareSAXWriter(writer);
        Serializer ser = new SAXSerializer(saxwr, writer);
        ser.emit(resultSeq);
        if (_wrap) {
            try {
                writer.write("\n</root>\n");
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
