class BackupThread extends Thread {
    public String request(int method, String url, Map<String, String> header, String body, boolean signed) throws Exception {
        String cacheKey = url;
        if (method == Http.GET) {
            Cache cache = cacheManager.get(cacheKey);
            if (cache != null) {
                logger.info("Found cache for " + cacheKey + " expires in " + DateFormat.getInstance().format(cache.getExpire()));
                return cache.getResponse();
            }
        }
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 6000);
        DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
        HttpRequestBase httpRequest;
        if (method == Http.GET) {
            httpRequest = new HttpGet(url);
        } else if (method == Http.POST) {
            httpRequest = new HttpPost(url);
            if (body != null && !body.isEmpty()) {
                ((HttpPost) httpRequest).setEntity(new StringEntity(body));
            }
        } else {
            throw new Exception("Invalid request method");
        }
        if (header != null) {
            Set<String> keys = header.keySet();
            for (String k : keys) {
                httpRequest.addHeader(k, header.get(k));
            }
        }
        if (oauth != null && signed) {
            oauth.signRequest(httpRequest);
        }
        logger.info("Request: " + httpRequest.getRequestLine().toString());
        HttpResponse httpResponse = httpClient.execute(httpRequest);
        logger.info("Response: " + httpResponse.getStatusLine().toString());
        HttpEntity entity = httpResponse.getEntity();
        String responseContent = EntityUtils.toString(entity);
        if (trafficListener != null) {
            TrafficItem trafficItem = new TrafficItem();
            trafficItem.setRequest(httpRequest);
            trafficItem.setRequestContent(body);
            trafficItem.setResponse(httpResponse);
            trafficItem.setResponseContent(responseContent);
            trafficListener.handleRequest(trafficItem);
        }
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (!(statusCode >= 200 && statusCode < 300)) {
            if (!responseContent.isEmpty()) {
                String message = responseContent.length() > 128 ? responseContent.substring(0, 128) + "..." : responseContent;
                throw new Exception(message);
            } else {
                throw new Exception("No successful status code");
            }
        }
        lastRequest = httpRequest;
        lastResponse = httpResponse;
        if (method == Http.GET) {
            Date expire = null;
            Header[] headers = httpResponse.getAllHeaders();
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].getName().toLowerCase().equals("expires")) {
                    try {
                        expire = DateFormat.getInstance().parse(headers[i].getValue());
                    } catch (Exception e) {
                    }
                }
            }
            if (expire != null && expire.compareTo(new Date()) > 0) {
                Cache cache = new Cache(cacheKey, responseContent, expire);
                cacheManager.add(cache);
                logger.info("Add to cache " + cacheKey + " expires in " + DateFormat.getInstance().format(expire));
            }
        }
        return responseContent;
    }
}
