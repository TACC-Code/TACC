class BackupThread extends Thread {
    public void fileUpload() throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        HttpPost httppost = new HttpPost("http://www.gigasize.com/uploadie");
        httppost.setHeader("Cookie", gigasizecookies.toString());
        MultipartEntity mpEntity = new MultipartEntity();
        mpEntity.addPart("UPLOAD_IDENTIFIER", new StringBody(uploadid));
        mpEntity.addPart("sid", new StringBody(sid));
        mpEntity.addPart("fileUpload1", new MonitoredFileBody(file, uploadProgress));
        httppost.setEntity(mpEntity);
        status = UploadStatus.UPLOADING;
        NULogger.getLogger().info("Now uploading your file into Gigasize...........................");
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        NULogger.getLogger().info(response.getStatusLine().toString());
        if (resEntity != null) {
            sid = "";
            sid = EntityUtils.toString(resEntity);
            NULogger.getLogger().log(Level.INFO, "After upload sid value : {0}", sid);
        } else {
            throw new Exception("There might be a problem with your internet connection or GigaSize server problem. Please try after some time :(");
        }
    }
}
