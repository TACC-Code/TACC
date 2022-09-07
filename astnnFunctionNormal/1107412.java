class BackupThread extends Thread {
    private String get(HttpRequestBase requestBase, ContentProcessable cp, HttpAction ha) throws IOException, CookieException, ProcessException {
        showCookies();
        debug(requestBase, ha, cp);
        String out = "";
        HttpResponse res = execute(requestBase);
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        try {
            Charset charSet = Charset.forName(ha.getCharset());
            br = new BufferedReader(new InputStreamReader(res.getEntity().getContent(), charSet));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } finally {
            if (br != null) br.close();
        }
        out = sb.toString();
        if (cp != null) {
            if (cp instanceof CookieValidateable && client instanceof DefaultHttpClient) ((CookieValidateable) cp).validateReturningCookies(cookieTransform(((DefaultHttpClient) client).getCookieStore().getCookies()), ha);
            out = cp.processReturningText(out, ha);
        }
        res.getEntity().consumeContent();
        return out;
    }
}
