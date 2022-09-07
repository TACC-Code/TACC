class BackupThread extends Thread {
    @Override
    public XLExprValue<?> eval(ExecutionContext context) throws XLWrapException, XLWrapEOFException {
        XLExprValue<?> v1 = args.get(0).eval(context);
        if (v1 == null) return null;
        String s = TypeCast.toString(v1);
        E_String cached = cache.get(s);
        E_String uri = null;
        if (cached == null) {
            HttpURLConnection c = null;
            BufferedReader io = null;
            try {
                URL url = new URL("http://swse.deri.org/list?keyword=" + URLEncoder.encode(s, "UTF-8"));
                c = (HttpURLConnection) url.openConnection();
                c.setConnectTimeout(5000);
                c.setReadTimeout(30000);
                c.setDoInput(true);
                c.connect();
                int code = c.getResponseCode();
                if (code > 300) throw new XLWrapException(URLDecoder.decode(c.getResponseMessage(), "UTF-8"));
                io = new BufferedReader(new InputStreamReader(c.getInputStream()));
                String line;
                while ((line = io.readLine()) != null) {
                    Matcher m = Pattern.compile("\\<\\/info\\>\\<list\\>\\<entry rdf\\:about\\=\\\"([^\\\"]*)\\\"").matcher(line);
                    if (m.find() && m.group(1) != null) uri = new E_String(m.group(1));
                }
                cache.put(s, uri);
                log.debug("New DBpedia link: '" + s + "' => <" + uri + ">");
            } catch (Exception e) {
                throw new XLWrapException("Failed to generate DBpedia URI from argument: " + v1, e);
            } finally {
                if (c != null) c.disconnect();
                if (io != null) try {
                    io.close();
                } catch (IOException e) {
                }
            }
        }
        return uri;
    }
}
