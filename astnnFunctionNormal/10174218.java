class BackupThread extends Thread {
    public byte[] filter(Request request, MimeHeaders headers, byte[] content) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            String md5 = Base64.encode(digest.digest(content));
            request.log(Server.LOG_DIAGNOSTIC, isMine.prefix(), "Digest for " + request.url + " " + md5);
            request.addHeader("Content-MD5", md5);
        } catch (NoSuchAlgorithmException e) {
        }
        return content;
    }
}
