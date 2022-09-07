class BackupThread extends Thread {
    protected static Principal findPrincipal(Request request, String authorization, Realm realm) {
        if (authorization == null) return (null);
        if (!authorization.startsWith("Digest ")) return (null);
        authorization = authorization.substring(7).trim();
        String[] tokens = authorization.split(",(?=(?:[^\"]*\"[^\"]*\")+$)");
        String userName = null;
        String realmName = null;
        String nOnce = null;
        String nc = null;
        String cnonce = null;
        String qop = null;
        String uri = null;
        String response = null;
        String method = request.getMethod();
        for (int i = 0; i < tokens.length; i++) {
            String currentToken = tokens[i];
            if (currentToken.length() == 0) continue;
            int equalSign = currentToken.indexOf('=');
            if (equalSign < 0) return null;
            String currentTokenName = currentToken.substring(0, equalSign).trim();
            String currentTokenValue = currentToken.substring(equalSign + 1).trim();
            if ("username".equals(currentTokenName)) userName = removeQuotes(currentTokenValue);
            if ("realm".equals(currentTokenName)) realmName = removeQuotes(currentTokenValue, true);
            if ("nonce".equals(currentTokenName)) nOnce = removeQuotes(currentTokenValue);
            if ("nc".equals(currentTokenName)) nc = removeQuotes(currentTokenValue);
            if ("cnonce".equals(currentTokenName)) cnonce = removeQuotes(currentTokenValue);
            if ("qop".equals(currentTokenName)) qop = removeQuotes(currentTokenValue);
            if ("uri".equals(currentTokenName)) uri = removeQuotes(currentTokenValue);
            if ("response".equals(currentTokenName)) response = removeQuotes(currentTokenValue);
        }
        if ((userName == null) || (realmName == null) || (nOnce == null) || (uri == null) || (response == null)) return null;
        String a2 = method + ":" + uri;
        byte[] buffer = null;
        synchronized (md5Helper) {
            buffer = md5Helper.digest(a2.getBytes());
        }
        String md5a2 = md5Encoder.encode(buffer);
        return (realm.authenticate(userName, response, nOnce, nc, cnonce, qop, realmName, md5a2));
    }
}
