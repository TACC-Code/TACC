class BackupThread extends Thread {
    private void sendRequest(String cswRequest) throws IOException {
        HttpURLConnection httpCon = null;
        InputStream responseStream = null;
        try {
            if (LOGGER.isLoggable(Level.FINER)) {
                StringBuffer sb = new StringBuffer();
                sb.append("Sending CSW request\n");
                sb.append(" url=").append(cswURL);
                sb.append("\n").append(cswRequest);
                LOGGER.finer(sb.toString());
            }
            URL url = new URL(cswURL);
            httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setRequestMethod("POST");
            httpCon.setConnectTimeout(10000);
            httpCon.setDoInput(true);
            httpCon.setDoOutput(true);
            httpCon.setUseCaches(false);
            httpCon.setRequestProperty("Connection", "Close");
            sendData(httpCon, cswRequest);
            int nResponseCode = httpCon.getResponseCode();
            if (LOGGER.isLoggable(Level.FINER)) {
                responseStream = httpCon.getInputStream();
                String sResponse = readCharacters(responseStream).toString();
                StringBuffer sb = new StringBuffer();
                sb.append("Read CSW response\n");
                sb.append(" url=").append(cswURL);
                sb.append(" responseCode=").append(nResponseCode);
                sb.append("\n").append(sResponse);
                LOGGER.finer(sb.toString());
            }
        } finally {
            try {
                if (responseStream != null) responseStream.close();
            } catch (Exception ef) {
            }
        }
    }
}
