class BackupThread extends Thread {
    public static void fileUpload() throws IOException {
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        file = new File("H:\\UploadingdotcomUploaderPlugin.java");
        HttpPost httppost = new HttpPost(postURL);
        MultipartEntity mpEntity = new MultipartEntity();
        ContentBody cbFile = new FileBody(file);
        mpEntity.addPart("sub", new StringBody("upload"));
        mpEntity.addPart("cookie", new StringBody(cookie));
        mpEntity.addPart("folder", new StringBody("0"));
        mpEntity.addPart("filecontent", cbFile);
        httppost.setEntity(mpEntity);
        System.out.println("Now uploading your file into rs...........................");
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        System.out.println(response.getStatusLine());
        if (resEntity != null) {
            uploadresponse = EntityUtils.toString(resEntity);
            System.out.println("Actual response : " + uploadresponse);
            uploadresponse = uploadresponse.replace("COMPLETE\n", "");
            downloadid = uploadresponse.substring(0, uploadresponse.indexOf(","));
            uploadresponse = uploadresponse.replace(downloadid + ",", "");
            filename = uploadresponse.substring(0, uploadresponse.indexOf(","));
            System.out.println("download id : " + downloadid);
            System.out.println("File name : " + filename);
            downloadlink = "http://rapidshare.com/files/" + downloadid + "/" + filename;
            System.out.println("Download Link :" + downloadlink);
        }
    }
}
