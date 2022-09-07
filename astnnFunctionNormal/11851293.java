class BackupThread extends Thread {
    public File doPostGetFile(String url, String postData, File inFile) throws IOException {
        long t1 = System.currentTimeMillis();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("##### doPost-start #####, url=" + url + ", postData=\n" + postData);
        }
        InputStream responseBodyInputStream = null;
        PostMethod pMethod = new PostMethod(url);
        if ("yes".equalsIgnoreCase(config.getProperty(XDriveConstant.CONFIG_HTTPCLIENT_IGNORECOOKIES))) {
            pMethod.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        }
        try {
            NameValuePair type = new NameValuePair("data", postData);
            pMethod.setRequestBody(new NameValuePair[] { type });
            this.hc.executeMethod(pMethod);
            responseBodyInputStream = pMethod.getResponseBodyAsStream();
            final int bufferSize = 2048;
            FileOutputStream fout = new FileOutputStream(inFile);
            byte[] buffer = new byte[bufferSize];
            int readCount = 0;
            while ((readCount = responseBodyInputStream.read(buffer)) != -1) {
                if (readCount < bufferSize) {
                    fout.write(buffer, 0, readCount);
                } else {
                    fout.write(buffer);
                }
            }
            fout.close();
        } finally {
            pMethod.releaseConnection();
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("##### doPost-end   #####, used time: " + (System.currentTimeMillis() - t1) + " ms,response=[InputStream]\n");
        }
        return inFile;
    }
}
