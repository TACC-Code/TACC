class BackupThread extends Thread {
    public String post(String URL, List<NameValuePair> params) throws Exception {
        String resultString;
        try {
            HttpPost httpRequest = new HttpPost(URL);
            httpRequest.setEntity(new UrlEncodedFormEntity(params, "GB2312"));
            HttpResponse httpResponse = client.execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                resultString = readstream(httpResponse.getEntity().getContent());
            } else {
                throw new Exception("can't connect the network");
            }
            return resultString;
        } catch (Exception e) {
            throw e;
        }
    }
}
