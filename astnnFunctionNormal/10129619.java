class BackupThread extends Thread {
    @Override
    public void execute(Request request, View... views) {
        if (isRequestStarted) {
            throw new MultipleRequestInSameRequestExecuterException("A RequestExecuter object can be used only once!");
        }
        isRequestStarted = true;
        for (View view : views) {
            view.doStart(request);
        }
        final URL url = request.getUrl();
        final String urlHost = url.getHost();
        final int urlPort = url.getPort() == -1 ? url.getDefaultPort() : url.getPort();
        final String urlProtocol = url.getProtocol();
        final String urlStr = url.toString();
        HttpContext httpContext = null;
        httpclient = new DefaultHttpClient();
        HTTPVersion httpVersion = request.getHttpVersion();
        ProtocolVersion protocolVersion = httpVersion == HTTPVersion.HTTP_1_1 ? new ProtocolVersion("HTTP", 1, 1) : new ProtocolVersion("HTTP", 1, 0);
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, protocolVersion);
        IGlobalOptions options = Implementation.of(IGlobalOptions.class);
        options.acquire();
        HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), Integer.parseInt(options.getProperty("request-timeout-in-millis")));
        options.release();
        ProxyConfig proxy = ProxyConfig.getInstance();
        proxy.acquire();
        if (proxy.isEnabled()) {
            final HttpHost proxyHost = new HttpHost(proxy.getHost(), proxy.getPort(), "http");
            if (proxy.isAuthEnabled()) {
                httpclient.getCredentialsProvider().setCredentials(new AuthScope(proxy.getHost(), proxy.getPort()), new UsernamePasswordCredentials(proxy.getUsername(), new String(proxy.getPassword())));
            }
            httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxyHost);
        }
        proxy.release();
        boolean authEnabled = request.getAuthMethods().size() > 0 ? true : false;
        if (authEnabled) {
            String uid = request.getAuthUsername();
            String pwd = new String(request.getAuthPassword());
            String host = StringUtil.isEmpty(request.getAuthHost()) ? urlHost : request.getAuthHost();
            String realm = StringUtil.isEmpty(request.getAuthRealm()) ? AuthScope.ANY_REALM : request.getAuthRealm();
            List<String> authPrefs = new ArrayList<String>(2);
            List<HTTPAuthMethod> authMethods = request.getAuthMethods();
            for (HTTPAuthMethod authMethod : authMethods) {
                switch(authMethod) {
                    case BASIC:
                        authPrefs.add(AuthPolicy.BASIC);
                        break;
                    case DIGEST:
                        authPrefs.add(AuthPolicy.DIGEST);
                        break;
                }
            }
            httpclient.getParams().setParameter("http.auth.scheme-pref", authPrefs);
            httpclient.getCredentialsProvider().setCredentials(new AuthScope(host, urlPort, realm), new UsernamePasswordCredentials(uid, pwd));
            if (request.isAuthPreemptive()) {
                BasicHttpContext localcontext = new BasicHttpContext();
                BasicScheme basicAuth = new BasicScheme();
                localcontext.setAttribute("preemptive-auth", basicAuth);
                httpclient.addRequestInterceptor(new PreemptiveAuth(), 0);
                httpContext = localcontext;
            }
        }
        AbstractHttpMessage method = null;
        final HTTPMethod httpMethod = request.getMethod();
        try {
            switch(httpMethod) {
                case GET:
                    method = new HttpGet(urlStr);
                    break;
                case POST:
                    method = new HttpPost(urlStr);
                    break;
                case PUT:
                    method = new HttpPut(urlStr);
                    break;
                case DELETE:
                    method = new HttpDelete(urlStr);
                    break;
                case HEAD:
                    method = new HttpHead(urlStr);
                    break;
                case OPTIONS:
                    method = new HttpOptions(urlStr);
                    break;
                case TRACE:
                    method = new HttpTrace(urlStr);
                    break;
            }
            method.setParams(new BasicHttpParams().setParameter(urlStr, url));
            MultiValueMap<String, String> header_data = request.getHeaders();
            for (String key : header_data.keySet()) {
                for (String value : header_data.get(key)) {
                    Header header = new BasicHeader(key, value);
                    method.addHeader(header);
                }
            }
            if (method instanceof HttpEntityEnclosingRequest) {
                HttpEntityEnclosingRequest eeMethod = (HttpEntityEnclosingRequest) method;
                ReqEntity bean = request.getBody();
                if (bean != null) {
                    try {
                        AbstractHttpEntity entity = new ByteArrayEntity(bean.getBody().getBytes(bean.getCharSet()));
                        entity.setContentType(bean.getContentTypeCharsetFormatted());
                        eeMethod.setEntity(entity);
                    } catch (UnsupportedEncodingException ex) {
                        for (View view : views) {
                            view.doError(Util.getStackTrace(ex));
                            view.doEnd();
                        }
                        return;
                    }
                }
            }
            SSLHostnameVerifier verifier = request.getSslHostNameVerifier();
            final X509HostnameVerifier hcVerifier;
            switch(verifier) {
                case STRICT:
                    hcVerifier = SSLSocketFactory.STRICT_HOSTNAME_VERIFIER;
                    break;
                case BROWSER_COMPATIBLE:
                    hcVerifier = SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
                    break;
                case ALLOW_ALL:
                    hcVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
                    break;
                default:
                    hcVerifier = SSLSocketFactory.STRICT_HOSTNAME_VERIFIER;
                    break;
            }
            if (urlProtocol.equalsIgnoreCase("https")) {
                final String trustStorePath = request.getSslTrustStore();
                final KeyStore trustStore = StringUtil.isEmpty(trustStorePath) ? null : getTrustStore(trustStorePath, request.getSslTrustStorePassword());
                SSLSocketFactory socketFactory = new SSLSocketFactory("TLS", null, null, trustStore, null, hcVerifier);
                Scheme sch = new Scheme(urlProtocol, urlPort, socketFactory);
                httpclient.getConnectionManager().getSchemeRegistry().register(sch);
            }
            httpclient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler());
            httpclient.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, request.isFollowRedirect());
            long startTime = System.currentTimeMillis();
            HttpResponse http_res = httpclient.execute((HttpUriRequest) method, httpContext);
            long endTime = System.currentTimeMillis();
            ResponseBean response = new ResponseBean();
            response.setExecutionTime(endTime - startTime);
            response.setStatusCode(http_res.getStatusLine().getStatusCode());
            response.setStatusLine(http_res.getStatusLine().toString());
            final Header[] responseHeaders = http_res.getAllHeaders();
            String contentType = null;
            for (Header header : responseHeaders) {
                response.addHeader(header.getName(), header.getValue());
                if (header.getName().equalsIgnoreCase("content-type")) {
                    contentType = header.getValue();
                }
            }
            final Charset charset;
            {
                Charset c;
                if (contentType != null) {
                    final String charsetStr = Util.getCharsetFromContentType(contentType);
                    try {
                        c = Charset.forName(charsetStr);
                    } catch (IllegalCharsetNameException ex) {
                        LOG.log(Level.WARNING, "Charset name is illegal: {0}", charsetStr);
                        c = Charset.defaultCharset();
                    } catch (UnsupportedCharsetException ex) {
                        LOG.log(Level.WARNING, "Charset {0} is not supported in this JVM.", charsetStr);
                        c = Charset.defaultCharset();
                    } catch (IllegalArgumentException ex) {
                        LOG.log(Level.WARNING, "Charset parameter is not available in Content-Type header!");
                        c = Charset.defaultCharset();
                    }
                } else {
                    c = Charset.defaultCharset();
                    LOG.log(Level.WARNING, "Content-Type header not available in response. Using platform default encoding: {0}", c.name());
                }
                charset = c;
            }
            final HttpEntity entity = http_res.getEntity();
            if (entity != null) {
                InputStream is = entity.getContent();
                try {
                    String responseBody = StreamUtil.inputStream2String(is, charset);
                    if (responseBody != null) {
                        response.setResponseBody(responseBody);
                    }
                } catch (IOException ex) {
                    final String msg = "Response body conversion to string using " + charset.displayName() + " encoding failed. Response body not set!";
                    for (View view : views) {
                        view.doError(msg);
                    }
                    LOG.log(Level.WARNING, msg);
                }
            }
            try {
                junit.framework.TestSuite suite = TestUtil.getTestSuite(request, response);
                if (suite != null) {
                    TestResult testResult = TestUtil.execute(suite);
                    response.setTestResult(testResult);
                }
            } catch (TestException ex) {
                for (View view : views) {
                    view.doError(Util.getStackTrace(ex));
                }
            }
            for (View view : views) {
                view.doResponse(response);
            }
        } catch (IOException ex) {
            if (!interruptedShutdown) {
                for (View view : views) {
                    view.doError(Util.getStackTrace(ex));
                }
            } else {
                for (View view : views) {
                    view.doCancelled();
                }
            }
        } catch (Exception ex) {
            if (!interruptedShutdown) {
                for (View view : views) {
                    view.doError(Util.getStackTrace(ex));
                }
            } else {
                for (View view : views) {
                    view.doCancelled();
                }
            }
        } finally {
            if (method != null && !interruptedShutdown) {
                httpclient.getConnectionManager().shutdown();
            }
            for (View view : views) {
                view.doEnd();
            }
            isRequestCompleted = true;
        }
    }
}
