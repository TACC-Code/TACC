class BackupThread extends Thread {
        SignatureOutputStream(Hessian2Output out) throws IOException {
            try {
                KeyGenerator keyGen = KeyGenerator.getInstance(_algorithm);
                if (_secureRandom != null) keyGen.init(_secureRandom);
                SecretKey sharedKey = keyGen.generateKey();
                _out = out;
                _out.startEnvelope(X509Signature.class.getName());
                PublicKey publicKey = _cert.getPublicKey();
                byte[] encoded = publicKey.getEncoded();
                MessageDigest md = MessageDigest.getInstance("SHA1");
                md.update(encoded);
                byte[] fingerprint = md.digest();
                String keyAlgorithm = _privateKey.getAlgorithm();
                Cipher keyCipher = Cipher.getInstance(keyAlgorithm);
                keyCipher.init(Cipher.WRAP_MODE, _privateKey);
                byte[] encKey = keyCipher.wrap(sharedKey);
                _out.writeInt(4);
                _out.writeString("algorithm");
                _out.writeString(_algorithm);
                _out.writeString("fingerprint");
                _out.writeBytes(fingerprint);
                _out.writeString("key-algorithm");
                _out.writeString(keyAlgorithm);
                _out.writeString("key");
                _out.writeBytes(encKey);
                _mac = Mac.getInstance(_algorithm);
                _mac.init(sharedKey);
                _bodyOut = _out.getBytesOutputStream();
            } catch (RuntimeException e) {
                throw e;
            } catch (IOException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
}
