class BackupThread extends Thread {
    private IResponseData getResponseData(String fileName, IResponseData.ResponseType type) throws IOException {
        DomRepresentation representation = ServerUtils.readDomRepresentation(ServerUtils.createFileName(DIGSTER_FOLDER + fileName));
        final DataDigster dataDigster = new DataDigster(type, representation);
        dataDigster.digest();
        return dataDigster.getResponseData();
    }
}
