class BackupThread extends Thread {
    public String get(String URL) throws Exception {
        String resultString;
        HttpGet sourceaddr = new HttpGet(URL);
        try {
            HttpResponse httpResponse = client.execute(sourceaddr);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                resultString = readstream(httpResponse.getEntity().getContent());
            } else {
                throw new Exception("can't connect the network");
            }
            return resultString.toString();
        } catch (Exception e) {
            throw e;
        }
    }
}
