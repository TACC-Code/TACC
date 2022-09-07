class BackupThread extends Thread {
    public HttpResponse execute(final HttpRequest request, final HttpHost targetHost, final HttpClientConnection conn) throws HttpException, IOException {
        this.context.setAttribute(ExecutionContext.HTTP_REQUEST, request);
        this.context.setAttribute(ExecutionContext.HTTP_TARGET_HOST, targetHost);
        this.context.setAttribute(ExecutionContext.HTTP_CONNECTION, conn);
        request.setParams(new DefaultedHttpParams(request.getParams(), this.params));
        this.httpexecutor.preProcess(request, this.httpproc, this.context);
        HttpResponse response = this.httpexecutor.execute(request, conn, this.context);
        response.setParams(new DefaultedHttpParams(response.getParams(), this.params));
        this.httpexecutor.postProcess(response, this.httpproc, this.context);
        return response;
    }
}
