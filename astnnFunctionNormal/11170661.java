class BackupThread extends Thread {
    public static void main(String[] args) throws Exception {
        HttpParams params = new BasicHttpParams();
        ConnManagerParams.setMaxTotalConnections(params, 100);
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
        HttpClient httpclient = new DefaultHttpClient(cm, params);
        String[] urisToGet = { "http://jakarta.apache.org/", "http://jakarta.apache.org/commons/", "http://jakarta.apache.org/commons/httpclient/", "http://svn.apache.org/viewvc/jakarta/httpcomponents/" };
        IdleConnectionEvictor connEvictor = new IdleConnectionEvictor(cm);
        connEvictor.start();
        for (int i = 0; i < urisToGet.length; i++) {
            String requestURI = urisToGet[i];
            HttpGet req = new HttpGet(requestURI);
            System.out.println("executing request " + requestURI);
            HttpResponse rsp = httpclient.execute(req);
            HttpEntity entity = rsp.getEntity();
            System.out.println("----------------------------------------");
            System.out.println(rsp.getStatusLine());
            if (entity != null) {
                System.out.println("Response content length: " + entity.getContentLength());
            }
            System.out.println("----------------------------------------");
            if (entity != null) {
                entity.consumeContent();
            }
        }
        Thread.sleep(20000);
        connEvictor.shutdown();
        connEvictor.join();
        httpclient.getConnectionManager().shutdown();
    }
}
