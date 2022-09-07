class BackupThread extends Thread {
    public HttpResponse execute(HttpHost target, HttpRequest request, HttpContext context) throws HttpException, IOException {
        HttpRequest orig = request;
        RequestWrapper origWrapper = wrapRequest(orig);
        origWrapper.setParams(params);
        HttpRoute origRoute = determineRoute(target, origWrapper, context);
        virtualHost = (HttpHost) orig.getParams().getParameter(ClientPNames.VIRTUAL_HOST);
        RoutedRequest roureq = new RoutedRequest(origWrapper, origRoute);
        long timeout = ConnManagerParams.getTimeout(params);
        int execCount = 0;
        boolean reuse = false;
        boolean done = false;
        try {
            HttpResponse response = null;
            while (!done) {
                RequestWrapper wrapper = roureq.getRequest();
                HttpRoute route = roureq.getRoute();
                response = null;
                Object userToken = context.getAttribute(ClientContext.USER_TOKEN);
                if (managedConn == null) {
                    ClientConnectionRequest connRequest = connManager.requestConnection(route, userToken);
                    if (orig instanceof AbortableHttpRequest) {
                        ((AbortableHttpRequest) orig).setConnectionRequest(connRequest);
                    }
                    try {
                        managedConn = connRequest.getConnection(timeout, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException interrupted) {
                        InterruptedIOException iox = new InterruptedIOException();
                        iox.initCause(interrupted);
                        throw iox;
                    }
                    if (HttpConnectionParams.isStaleCheckingEnabled(params)) {
                        if (managedConn.isOpen()) {
                            this.log.debug("Stale connection check");
                            if (managedConn.isStale()) {
                                this.log.debug("Stale connection detected");
                                managedConn.close();
                            }
                        }
                    }
                }
                if (orig instanceof AbortableHttpRequest) {
                    ((AbortableHttpRequest) orig).setReleaseTrigger(managedConn);
                }
                if (!managedConn.isOpen()) {
                    managedConn.open(route, context, params);
                }
                try {
                    establishRoute(route, context);
                } catch (TunnelRefusedException ex) {
                    if (this.log.isDebugEnabled()) {
                        this.log.debug(ex.getMessage());
                    }
                    response = ex.getResponse();
                    break;
                }
                wrapper.resetHeaders();
                rewriteRequestURI(wrapper, route);
                target = virtualHost;
                if (target == null) {
                    target = route.getTargetHost();
                }
                HttpHost proxy = route.getProxyHost();
                context.setAttribute(ExecutionContext.HTTP_TARGET_HOST, target);
                context.setAttribute(ExecutionContext.HTTP_PROXY_HOST, proxy);
                context.setAttribute(ExecutionContext.HTTP_CONNECTION, managedConn);
                context.setAttribute(ClientContext.TARGET_AUTH_STATE, targetAuthState);
                context.setAttribute(ClientContext.PROXY_AUTH_STATE, proxyAuthState);
                requestExec.preProcess(wrapper, httpProcessor, context);
                boolean retrying = true;
                Exception retryReason = null;
                while (retrying) {
                    execCount++;
                    wrapper.incrementExecCount();
                    if (wrapper.getExecCount() > 1 && !wrapper.isRepeatable()) {
                        this.log.debug("Cannot retry non-repeatable request");
                        if (retryReason != null) {
                            throw new NonRepeatableRequestException("Cannot retry request " + "with a non-repeatable request entity.  The cause lists the " + "reason the original request failed.", retryReason);
                        } else {
                            throw new NonRepeatableRequestException("Cannot retry request " + "with a non-repeatable request entity.");
                        }
                    }
                    try {
                        if (this.log.isDebugEnabled()) {
                            this.log.debug("Attempt " + execCount + " to execute request");
                        }
                        response = requestExec.execute(wrapper, managedConn, context);
                        retrying = false;
                    } catch (IOException ex) {
                        this.log.debug("Closing the connection.");
                        managedConn.close();
                        if (retryHandler.retryRequest(ex, execCount, context)) {
                            if (this.log.isInfoEnabled()) {
                                this.log.info("I/O exception (" + ex.getClass().getName() + ") caught when processing request: " + ex.getMessage());
                            }
                            if (this.log.isDebugEnabled()) {
                                this.log.debug(ex.getMessage(), ex);
                            }
                            this.log.info("Retrying request");
                            retryReason = ex;
                        } else {
                            throw ex;
                        }
                        if (!route.isTunnelled()) {
                            this.log.debug("Reopening the direct connection.");
                            managedConn.open(route, context, params);
                        } else {
                            this.log.debug("Proxied connection. Need to start over.");
                            retrying = false;
                        }
                    }
                }
                if (response == null) {
                    continue;
                }
                response.setParams(params);
                requestExec.postProcess(response, httpProcessor, context);
                reuse = reuseStrategy.keepAlive(response, context);
                if (reuse) {
                    long duration = keepAliveStrategy.getKeepAliveDuration(response, context);
                    managedConn.setIdleDuration(duration, TimeUnit.MILLISECONDS);
                    if (this.log.isDebugEnabled()) {
                        if (duration >= 0) {
                            this.log.debug("Connection can be kept alive for " + duration + " ms");
                        } else {
                            this.log.debug("Connection can be kept alive indefinitely");
                        }
                    }
                }
                RoutedRequest followup = handleResponse(roureq, response, context);
                if (followup == null) {
                    done = true;
                } else {
                    if (reuse) {
                        HttpEntity entity = response.getEntity();
                        if (entity != null) {
                            entity.consumeContent();
                        }
                        managedConn.markReusable();
                    } else {
                        managedConn.close();
                    }
                    if (!followup.getRoute().equals(roureq.getRoute())) {
                        releaseConnection();
                    }
                    roureq = followup;
                }
                if (managedConn != null && userToken == null) {
                    userToken = userTokenHandler.getUserToken(context);
                    context.setAttribute(ClientContext.USER_TOKEN, userToken);
                    if (userToken != null) {
                        managedConn.setState(userToken);
                    }
                }
            }
            if ((response == null) || (response.getEntity() == null) || !response.getEntity().isStreaming()) {
                if (reuse) managedConn.markReusable();
                releaseConnection();
            } else {
                HttpEntity entity = response.getEntity();
                entity = new BasicManagedEntity(entity, managedConn, reuse);
                response.setEntity(entity);
            }
            return response;
        } catch (HttpException ex) {
            abortConnection();
            throw ex;
        } catch (IOException ex) {
            abortConnection();
            throw ex;
        } catch (RuntimeException ex) {
            abortConnection();
            throw ex;
        }
    }
}
