class BackupThread extends Thread {
    public void write(String pURL, List<File> pValue) throws IOException {
        open(pURL);
        MultipartEntity reqEntity = new MultipartEntity();
        for (File t : pValue) {
            FileBody bin = new FileBody(t);
            reqEntity.addPart(t.getName(), bin);
        }
        httpPost.setEntity(reqEntity);
        HttpResponse response = httpclient.execute(httpPost);
    }
}
