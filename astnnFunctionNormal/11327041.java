class BackupThread extends Thread {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("File path not given");
            System.exit(1);
        }
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://localhost:8080" + "/servlets-examples/servlet/RequestInfoExample");
        File file = new File(args[0]);
        InputStreamEntity reqEntity = new InputStreamEntity(new FileInputStream(file), -1);
        reqEntity.setContentType("binary/octet-stream");
        reqEntity.setChunked(true);
        httppost.setEntity(reqEntity);
        System.out.println("executing request " + httppost.getRequestLine());
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        System.out.println("----------------------------------------");
        System.out.println(response.getStatusLine());
        if (resEntity != null) {
            System.out.println("Response content length: " + resEntity.getContentLength());
            System.out.println("Chunked?: " + resEntity.isChunked());
        }
        if (resEntity != null) {
            resEntity.consumeContent();
        }
    }
}
