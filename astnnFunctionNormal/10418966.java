class BackupThread extends Thread {
    public Object handleIt(HashMap<String, Object> parametersMap) {
        ArrayList<Object> parameters = (ArrayList<Object>) parametersMap.get("parameters");
        URL checkedURL = (URL) parameters.get(1);
        HttpClient httpclient = new DefaultHttpClient();
        String retVal = null;
        try {
            HttpGet httpget = new HttpGet(checkedURL.toURI());
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                retVal = EntityUtils.toString(responseEntity, HTTP.UTF_8);
            }
        } catch (Exception e) {
            parameters.add(e);
            retVal = null;
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        if (retVal == null) {
            QuickConnect.handleError("getRequestFailure", parametersMap);
        }
        retVal = retVal.replaceAll("\"", "'");
        return retVal;
    }
}
