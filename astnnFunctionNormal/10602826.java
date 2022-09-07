class BackupThread extends Thread {
    private Element sendXMLRequest(String uri) throws Exception {
        if (http == null) http = new DefaultHttpClient();
        HttpGet request = new HttpGet(uri);
        HttpResponse response = http.execute(request);
        InputStream is = response.getEntity().getContent();
        if (dbf == null) dbf = DocumentBuilderFactory.newInstance();
        if (db == null) db = dbf.newDocumentBuilder();
        Document doc = db.parse(is);
        Element e = doc.getDocumentElement();
        e.normalize();
        return e;
    }
}
