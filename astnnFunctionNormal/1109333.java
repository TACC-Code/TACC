class BackupThread extends Thread {
    public static void fileUpload() throws IOException {
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        file = new File("C:\\Documents and Settings\\dinesh\\Desktop\\GrUploaderPlugin.java");
        HttpPost httppost = new HttpPost(gruploadlink);
        if (login) {
            httppost.setHeader("Cookie", logincookie + ";" + xfsscookie);
        }
        MultipartEntity mpEntity = new MultipartEntity();
        ContentBody cbFile = new FileBody(file);
        mpEntity.addPart("upload_type", new StringBody("file"));
        if (login) {
            mpEntity.addPart("sess_id", new StringBody(xfsscookie.substring(5)));
        }
        mpEntity.addPart("srv_tmp_url", new StringBody(tmpserver + "/tmp"));
        mpEntity.addPart("file_0", cbFile);
        httppost.setEntity(mpEntity);
        System.out.println("Now uploading your file into grupload...........................");
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        System.out.println(response.getStatusLine());
        if (resEntity != null) {
            String tmp = EntityUtils.toString(resEntity);
            System.out.println("Upload response : " + tmp);
            fnvalue = parseResponse(tmp, "name='fn'>", "<");
            System.out.println("fn value : " + fnvalue);
        }
    }
}
