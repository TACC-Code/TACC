class BackupThread extends Thread {
    @Override
    protected void doFetch(HttpServletRequest request, HttpServletResponse response) throws GadgetException, IOException {
        HttpRequest rcr = buildHttpRequest(request);
        HttpResponse results = requestPipeline.execute(rcr);
        if (contentRewriterRegistry != null) {
            try {
                results = contentRewriterRegistry.rewriteHttpResponse(rcr, results);
            } catch (RewritingException e) {
                throw new GadgetException(GadgetException.Code.INTERNAL_SERVER_ERROR, e);
            }
        }
        String output = convertResponseToJson(rcr.getSecurityToken(), request, results);
        setResponseHeaders(request, response, results);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(UNPARSEABLE_CRUFT + output);
    }
}
