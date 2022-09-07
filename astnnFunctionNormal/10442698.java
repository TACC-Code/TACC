class BackupThread extends Thread {
    public GetRecordByIdResponseType GetRecordById(String id) throws IOException, XmlException {
        GetRecordByIdResponseType getRecordByIdResponse = null;
        String getRequest = serviceEndPoint + "?request=GetRecordById&ElementSetName=full&id=" + id;
        System.out.println("getRequest " + getRequest);
        if (Navigator.isVerbose()) {
            System.out.println("contacting " + getRequest);
        }
        URL url = new URL(getRequest);
        URLConnection urlc = url.openConnection();
        urlc.setReadTimeout(Navigator.TIME_OUT);
        InputStream urlIn = urlc.getInputStream();
        GetRecordByIdResponseDocument obsCollDoc = GetRecordByIdResponseDocument.Factory.parse(urlIn);
        getRecordByIdResponse = obsCollDoc.getGetRecordByIdResponse();
        return getRecordByIdResponse;
    }
}
