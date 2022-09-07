class BackupThread extends Thread {
    private void fileUpload() throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(postURL);
        MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        mpEntity.addPart("upload_type", new StringBody("file"));
        mpEntity.addPart("sess_id", new StringBody(sessid));
        mpEntity.addPart("srv_tmp_url", new StringBody(servertmpurl));
        mpEntity.addPart("file_0", new MonitoredFileBody(file, uploadProgress));
        mpEntity.addPart("submit_btn", new StringBody(" Upload!"));
        httppost.setEntity(mpEntity);
        NULogger.getLogger().log(Level.INFO, "executing request {0}", httppost.getRequestLine());
        NULogger.getLogger().info("Now uploading your file into enterupload.com");
        status = UploadStatus.UPLOADING;
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        NULogger.getLogger().info(response.getStatusLine().toString());
        if (resEntity != null) {
            uploadresponse = EntityUtils.toString(resEntity);
        }
        downloadid = parseResponse(uploadresponse, "<textarea name='fn'>", "<");
        httpclient.getConnectionManager().shutdown();
    }
}
