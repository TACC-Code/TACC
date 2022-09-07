class BackupThread extends Thread {
    private void fileUpload() throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(postURL);
        MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        mpEntity.addPart("file", new MonitoredFileBody(file, uploadProgress));
        httppost.setEntity(mpEntity);
        NULogger.getLogger().log(Level.INFO, "executing request {0}", httppost.getRequestLine());
        status = UploadStatus.UPLOADING;
        NULogger.getLogger().info("Now uploading your file into bayfiles.com");
        HttpResponse response = httpclient.execute(httppost);
        status = UploadStatus.GETTINGLINK;
        HttpEntity resEntity = response.getEntity();
        NULogger.getLogger().info(response.getStatusLine().toString());
        if (resEntity != null) {
            uploadresponse = EntityUtils.toString(resEntity);
        }
        NULogger.getLogger().log(Level.INFO, "Upload response : {0}", uploadresponse);
        downloadlink = CommonUploaderTasks.parseResponse(uploadresponse, "\"downloadUrl\":\"", "\"");
        downloadlink = downloadlink.replaceAll("\\\\", "");
        deletelink = CommonUploaderTasks.parseResponse(uploadresponse, "\"deleteUrl\":\"", "\"");
        deletelink = deletelink.replaceAll("\\\\", "");
        NULogger.getLogger().log(Level.INFO, "Download link : {0}", downloadlink);
        NULogger.getLogger().log(Level.INFO, "Delete link : {0}", deletelink);
        downURL = downloadlink;
        delURL = deletelink;
        uploadFinished();
    }
}
