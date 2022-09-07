class BackupThread extends Thread {
    public IResponse executeMethod(HeaderBlock headerBlock, IMethod method) {
        Method request = new Method(method);
        if (headerBlock != null) {
            for (IHeader header : headerBlock.getHeaders()) {
                request.addHeader(header.getName(), header.getValue());
            }
        }
        if (method instanceof IEntityMethod) {
            try {
                request.setEntity(new StringEntity(((IEntityMethod) method).getEntity()));
                request.getParams().setParameter("http.protocol.expect-continue", false);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        if (debug) {
            System.out.println(request);
        }
        try {
            HttpResponse response = client.execute(host, request);
            return new StreamResponse(response);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
