class BackupThread extends Thread {
    public void publish(final Element cruisecontrolLog) throws CruiseControlException {
        HttpURLConnection conn = null;
        try {
            if ("GET".equals(this.requestMethod)) {
                String dataSet = createDataSet(cruisecontrolLog);
                if (dataSet.length() > 0) {
                    dataSet = "?" + dataSet;
                }
                url = new URL(this.urlString + dataSet);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod(this.requestMethod);
            } else {
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod(this.requestMethod);
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(createDataSet(cruisecontrolLog));
                wr.flush();
            }
            LOG.info("Sending " + this.requestMethod + " to " + this.urlString);
            conn.connect();
            LOG.info("Returned: " + conn.getResponseCode() + " : " + conn.getResponseMessage());
        } catch (IOException e) {
            throw new CruiseControlException(e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
