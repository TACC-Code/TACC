class BackupThread extends Thread {
    public GetRecordsResponseType findServices() throws IOException, XmlException {
        GetRecordsResponseType getRecordsResponse = null;
        String getRequest = serviceEndPoint + "?request=Query&qid=urn:ogc:def:ebRIM-Query:OGC:findServices";
        System.out.println("getRequest " + getRequest);
        if (Navigator.isVerbose()) {
            System.out.println("contacting " + getRequest);
        }
        URL url = new URL(getRequest);
        URLConnection urlc = url.openConnection();
        urlc.setReadTimeout(Navigator.TIME_OUT);
        InputStream urlIn = urlc.getInputStream();
        GetRecordsResponseDocument obsCollDoc = GetRecordsResponseDocument.Factory.parse(urlIn);
        getRecordsResponse = obsCollDoc.getGetRecordsResponse();
        return getRecordsResponse;
    }
}
