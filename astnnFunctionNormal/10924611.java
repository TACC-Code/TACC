class BackupThread extends Thread {
    public HttpURLConnection execute(Method method, List<String> vars, File payload) throws Exception {
        String url = toString();
        StringBuilder queryString = new StringBuilder();
        Map<String, List<String>> queryMap = Util.toMap(vars, '=');
        for (Map.Entry<String, List<String>> entry : queryMap.entrySet()) {
            for (String value : entry.getValue()) {
                if (queryString.length() > 0) queryString.append('&');
                queryString.append(entry.getKey()).append('=').append(value);
            }
        }
        populate(ParamStyle.QUERY, null, queryString, resource.getParam(), queryMap);
        Request request = method.getRequest();
        if (request != null) populate(ParamStyle.QUERY, null, queryString, request.getParam(), queryMap);
        if (queryString.length() > 0) url += "?" + queryString;
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod(method.getName());
        Map<String, List<String>> headerMap = Util.toMap(vars, '=');
        for (Map.Entry<String, List<String>> entry : headerMap.entrySet()) {
            for (String value : entry.getValue()) con.addRequestProperty(entry.getKey(), value);
        }
        populate(ParamStyle.HEADER, con, null, resource.getParam(), headerMap);
        if (request != null) {
            populate(ParamStyle.HEADER, con, null, request.getParam(), headerMap);
            if (!request.getRepresentation().isEmpty()) {
                Representation rep = request.getRepresentation().get(RandomUtil.random(0, request.getRepresentation().size() - 1));
                if (rep.getMediaType() != null) con.addRequestProperty("Content-Type", rep.getMediaType());
            }
        }
        con.addRequestProperty("Connection", "close");
        Authenticator authenticator = getAuthenticator();
        if (authenticator != null) authenticator.authenticate(con);
        if (payload != null) con.setDoOutput(true);
        con.connect();
        if (payload != null) IOUtil.pump(new FileInputStream(payload), con.getOutputStream(), true, false);
        return con;
    }
}
