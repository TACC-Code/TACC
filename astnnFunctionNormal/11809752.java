class BackupThread extends Thread {
    protected void setAuthenticateHeader(Request request, Response response, LoginConfig config, String nOnce) {
        String realmName = config.getRealmName();
        if (realmName == null) realmName = request.getServerName() + ":" + request.getServerPort();
        byte[] buffer = null;
        synchronized (md5Helper) {
            buffer = md5Helper.digest(nOnce.getBytes());
        }
        String authenticateHeader = "Digest realm=\"" + realmName + "\", " + "qop=\"auth\", nonce=\"" + nOnce + "\", " + "opaque=\"" + md5Encoder.encode(buffer) + "\"";
        response.setHeader("WWW-Authenticate", authenticateHeader);
    }
}
