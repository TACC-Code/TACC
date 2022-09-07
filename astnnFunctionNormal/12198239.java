class BackupThread extends Thread {
    public void fileUpload() throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        HttpPost httppost = new HttpPost(postURL);
        MultipartEntity mpEntity = new MultipartEntity();
        mpEntity.addPart("sub", new StringBody("upload"));
        mpEntity.addPart("cookie", new StringBody(RapidShareAccount.getRscookie()));
        mpEntity.addPart("folder", new StringBody("0"));
        mpEntity.addPart("filecontent", new MonitoredFileBody(file, uploadProgress));
        httppost.setEntity(mpEntity);
        status = UploadStatus.UPLOADING;
        NULogger.getLogger().info("Now uploading your file into rs...........................");
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        NULogger.getLogger().info(response.getStatusLine().toString());
        if (resEntity != null) {
            status = UploadStatus.GETTINGLINK;
            uploadresponse = EntityUtils.toString(resEntity);
            NULogger.getLogger().log(Level.INFO, "Actual response : {0}", uploadresponse);
            uploadresponse = uploadresponse.replace("COMPLETE\n", "");
            downloadid = uploadresponse.substring(0, uploadresponse.indexOf(","));
            uploadresponse = uploadresponse.replace(downloadid + ",", "");
            filename = uploadresponse.substring(0, uploadresponse.indexOf(","));
            NULogger.getLogger().log(Level.INFO, "download id : {0}", downloadid);
            NULogger.getLogger().log(Level.INFO, "File name : {0}", filename);
            downloadlink = "http://rapidshare.com/files/" + downloadid + "/" + filename;
            NULogger.getLogger().log(Level.INFO, "Download Link :{0}", downloadlink);
            downURL = downloadlink;
            uploadFinished();
        }
    }
}
