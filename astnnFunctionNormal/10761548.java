class BackupThread extends Thread {
    protected long proxyPlainTextRequest(URL url, String pathInContext, String pathParams, HttpRequest request, HttpResponse response) throws IOException {
        CaptureNetworkTrafficCommand.Entry entry = new CaptureNetworkTrafficCommand.Entry(request.getMethod(), url.toString());
        entry.addRequestHeaders(request);
        if (log.isDebugEnabled()) log.debug("PROXY URL=" + url);
        URLConnection connection = url.openConnection();
        connection.setAllowUserInteraction(false);
        if (proxyInjectionMode) {
            adjustRequestForProxyInjection(request, connection);
        }
        HttpURLConnection http = null;
        if (connection instanceof HttpURLConnection) {
            http = (HttpURLConnection) connection;
            http.setRequestMethod(request.getMethod());
            http.setInstanceFollowRedirects(false);
            if (trustAllSSLCertificates && connection instanceof HttpsURLConnection) {
                TrustEverythingSSLTrustManager.trustAllSSLCertificates((HttpsURLConnection) connection);
            }
        }
        String connectionHdr = request.getField(HttpFields.__Connection);
        if (connectionHdr != null && (connectionHdr.equalsIgnoreCase(HttpFields.__KeepAlive) || connectionHdr.equalsIgnoreCase(HttpFields.__Close))) connectionHdr = null;
        boolean xForwardedFor = false;
        boolean isGet = "GET".equals(request.getMethod());
        boolean hasContent = false;
        Enumeration enm = request.getFieldNames();
        while (enm.hasMoreElements()) {
            String hdr = (String) enm.nextElement();
            if (_DontProxyHeaders.containsKey(hdr) || !_chained && _ProxyAuthHeaders.containsKey(hdr)) continue;
            if (connectionHdr != null && connectionHdr.indexOf(hdr) >= 0) continue;
            if (!isGet && HttpFields.__ContentType.equals(hdr)) hasContent = true;
            Enumeration vals = request.getFieldValues(hdr);
            while (vals.hasMoreElements()) {
                String val = (String) vals.nextElement();
                if (val != null) {
                    if ("Referer".equals(hdr) && (-1 != val.indexOf("/selenium-server/"))) {
                        continue;
                    }
                    if (!isGet && HttpFields.__ContentLength.equals(hdr) && Integer.parseInt(val) > 0) {
                        hasContent = true;
                    }
                    connection.addRequestProperty(hdr, val);
                    xForwardedFor |= HttpFields.__XForwardedFor.equalsIgnoreCase(hdr);
                }
            }
        }
        Map<String, String> customRequestHeaders = AddCustomRequestHeaderCommand.getHeaders();
        for (Map.Entry<String, String> e : customRequestHeaders.entrySet()) {
            connection.addRequestProperty(e.getKey(), e.getValue());
            entry.addRequestHeader(e.getKey(), e.getValue());
        }
        if (!_anonymous) connection.setRequestProperty("Via", "1.1 (jetty)");
        if (!xForwardedFor) connection.addRequestProperty(HttpFields.__XForwardedFor, request.getRemoteAddr());
        String cache_control = request.getField(HttpFields.__CacheControl);
        if (cache_control != null && (cache_control.indexOf("no-cache") >= 0 || cache_control.indexOf("no-store") >= 0)) connection.setUseCaches(false);
        customizeConnection(pathInContext, pathParams, request, connection);
        try {
            connection.setDoInput(true);
            InputStream in = request.getInputStream();
            if (hasContent) {
                connection.setDoOutput(true);
                IO.copy(in, connection.getOutputStream());
            }
            connection.connect();
        } catch (Exception e) {
            LogSupport.ignore(log, e);
        }
        InputStream proxy_in = null;
        int code = -1;
        if (http != null) {
            proxy_in = http.getErrorStream();
            try {
                code = http.getResponseCode();
            } catch (SSLHandshakeException e) {
                throw new RuntimeException("Couldn't establish SSL handshake.  Try using trustAllSSLCertificates.\n" + e.getLocalizedMessage(), e);
            }
            response.setStatus(code);
            response.setReason(http.getResponseMessage());
            String contentType = http.getContentType();
            if (log.isDebugEnabled()) {
                log.debug("Content-Type is: " + contentType);
            }
        }
        if (proxy_in == null) {
            try {
                proxy_in = connection.getInputStream();
            } catch (Exception e) {
                LogSupport.ignore(log, e);
                proxy_in = http.getErrorStream();
            }
        }
        response.removeField(HttpFields.__Date);
        response.removeField(HttpFields.__Server);
        int h = 0;
        String hdr = connection.getHeaderFieldKey(h);
        String val = connection.getHeaderField(h);
        while (hdr != null || val != null) {
            if (hdr != null && val != null && !_DontProxyHeaders.containsKey(hdr) && (_chained || !_ProxyAuthHeaders.containsKey(hdr))) response.addField(hdr, val);
            h++;
            hdr = connection.getHeaderFieldKey(h);
            val = connection.getHeaderField(h);
        }
        if (!_anonymous) response.setField("Via", "1.1 (jetty)");
        response.removeField(HttpFields.__ETag);
        response.removeField(HttpFields.__LastModified);
        long bytesCopied = -1;
        request.setHandled(true);
        if (proxy_in != null) {
            boolean injectableResponse = http.getResponseCode() == HttpURLConnection.HTTP_OK || (http.getResponseCode() >= 400 && http.getResponseCode() < 600);
            if (proxyInjectionMode && injectableResponse) {
                if (shouldInject(request.getPath())) {
                    bytesCopied = InjectionHelper.injectJavaScript(request, response, proxy_in, response.getOutputStream(), debugURL);
                } else {
                    bytesCopied = ModifiedIO.copy(proxy_in, response.getOutputStream());
                }
            } else {
                bytesCopied = ModifiedIO.copy(proxy_in, response.getOutputStream());
            }
        }
        entry.finish(code, bytesCopied);
        entry.addResponseHeader(response);
        CaptureNetworkTrafficCommand.capture(entry);
        return bytesCopied;
    }
}
