class BackupThread extends Thread {
    private String post(HttpRequestBase requestBase, ContentProcessable contentProcessable, HttpAction ha) throws IOException, CookieException, ProcessException {
        Post p = (Post) ha;
        MultipartEntity entity = new MultipartEntity();
        for (String key : p.getParams().keySet()) {
            Object content = p.getParams().get(key);
            if (content != null) {
                if (content instanceof String) entity.addPart(key, new StringBody((String) content, Charset.forName(p.getCharset()))); else if (content instanceof File) entity.addPart(key, new FileBody((File) content));
            }
        }
        ((HttpPost) requestBase).setEntity(entity);
        debug(requestBase, ha, contentProcessable);
        HttpResponse res = execute(requestBase);
        ByteArrayOutputStream byte1 = new ByteArrayOutputStream();
        res.getEntity().writeTo(byte1);
        String out = new String(byte1.toByteArray());
        out = contentProcessable.processReturningText(out, ha);
        if (contentProcessable instanceof CookieValidateable && client instanceof DefaultHttpClient) ((CookieValidateable) contentProcessable).validateReturningCookies(cookieTransform(((DefaultHttpClient) client).getCookieStore().getCookies()), ha);
        res.getEntity().consumeContent();
        return out;
    }
}
