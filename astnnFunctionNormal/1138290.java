class BackupThread extends Thread {
    private HttpResponse executeHttp(HttpRequestBase req) throws IOException {
        try {
            for (int i = 0; i < 4; i++) {
                HttpResponse res = this.httpClient.execute(req);
                int status = res.getStatusLine().getStatusCode();
                if (300 <= status && status <= 399) {
                    req.abort();
                    URI location = httpClient.getRedirectHandler().getLocationURI(res, new BasicHttpContext());
                    req = new HttpGet(location);
                    req.setHeader("User-Agent", "Mozilla/4.0 (compatible;MSIE 7.0; Windows NT 6.0;)");
                } else {
                    return res;
                }
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
            log.info("HttpClient Fatal Error. Restarting HttpCient");
            HttpParams params = this.httpClient.getParams();
            this.httpClient.getConnectionManager().shutdown();
            this.httpClient = new DefaultHttpClient();
            this.httpClient.setParams(params);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            req.abort();
        }
        return null;
    }
}
