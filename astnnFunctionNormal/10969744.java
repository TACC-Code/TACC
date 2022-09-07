class BackupThread extends Thread {
    @Override
    public void run() {
        long time1 = System.currentTimeMillis();
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpHost prox = new HttpHost(host, port);
            client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, prox);
            String target = "http://www.google.de/intl/en_com/images/srpr/logo1w.png";
            HttpGet httpget = new HttpGet(target);
            HttpResponse response = client.execute(httpget);
            HttpEntity e = response.getEntity();
            BufferedReader rd = new BufferedReader(new InputStreamReader(e.getContent()));
            ;
            while (rd.readLine() != null) {
            }
            long time2 = System.currentTimeMillis();
            if ((time2 - time1) < proxyMaxDelay) {
                System.out.println("Added " + host + ":" + port + " with " + (time2 - time1) + "ms");
                ref.proxyList.add(new Proxy(host, port));
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }
    }
}
