class BackupThread extends Thread {
    private byte[] deriveKey(byte[] salt, int keylen) throws IOException {
        CallbackHandler passwordHandler = new ConsoleCallbackHandler();
        try {
            Class c = Class.forName(Security.getProperty("jessie.password.handler"));
            passwordHandler = (CallbackHandler) c.newInstance();
        } catch (Exception x) {
        }
        PasswordCallback passwdCallback = new PasswordCallback("Enter PEM passphrase: ", false);
        try {
            passwordHandler.handle(new Callback[] { passwdCallback });
        } catch (UnsupportedCallbackException uce) {
            throw new IOException("specified handler cannot handle passwords");
        }
        char[] passwd = passwdCallback.getPassword();
        IMessageDigest md5 = HashFactory.getInstance("MD5");
        byte[] key = new byte[keylen];
        int count = 0;
        while (count < keylen) {
            for (int i = 0; i < passwd.length; i++) md5.update((byte) passwd[i]);
            md5.update(salt, 0, salt.length);
            byte[] digest = md5.digest();
            int len = Math.min(digest.length, keylen - count);
            System.arraycopy(digest, 0, key, count, len);
            count += len;
            if (count >= keylen) break;
            md5.reset();
            md5.update(digest, 0, digest.length);
        }
        passwdCallback.clearPassword();
        return key;
    }
}
