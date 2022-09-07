class BackupThread extends Thread {
    private final boolean authenticate() {
        Message authMsg = readMessage();
        byte[] authSharedKey = new byte[0];
        try {
            authSharedKey = clientConfig.getAuthSharedKey().getBytes(clientConfig.getStringEncoding());
        } catch (UnsupportedEncodingException e) {
            System.out.println("Authentication error: " + e.getMessage());
        }
        byte[] authChallenge = new byte[authMsg.getMessageLength() + authSharedKey.length];
        System.arraycopy(authMsg.getMessageBytes(), 0, authChallenge, 0, authMsg.getMessageLength());
        System.arraycopy(authSharedKey, 0, authChallenge, authMsg.getMessageLength(), authSharedKey.length);
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Authentication error: " + e.getMessage());
        }
        byte[] authResponse = md.digest(authChallenge);
        sendMessage(new Message(Protocol.MSG_AUTHCHALLENGE_RESP, messageCounter++, authResponse.length, authResponse));
        Message authServerResponse = readMessage();
        if (authServerResponse.getMessageFlag() == Protocol.MSG_OK) {
            return true;
        } else {
            return false;
        }
    }
}
