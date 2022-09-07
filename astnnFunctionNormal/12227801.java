class BackupThread extends Thread {
    private void cancelOperation() {
        token.ensureValid();
        if (initialized == false) {
            return;
        }
        initialized = false;
        if ((session == null) || (token.explicitCancel == false)) {
            return;
        }
        if (session.hasObjects() == false) {
            session = token.killSession(session);
            return;
        }
        if (mode == M_SIGN) {
            try {
                if (type == T_UPDATE) {
                    token.p11.C_SignFinal(session.id(), 0);
                } else {
                    byte[] digest;
                    if (type == T_DIGEST) {
                        digest = md.digest();
                    } else {
                        digest = buffer;
                    }
                    token.p11.C_Sign(session.id(), digest);
                }
            } catch (PKCS11Exception e) {
                throw new ProviderException("cancel failed", e);
            }
        } else {
            try {
                byte[] signature;
                if (keyAlgorithm.equals("DSA")) {
                    signature = new byte[40];
                } else {
                    signature = new byte[(p11Key.keyLength() + 7) >> 3];
                }
                if (type == T_UPDATE) {
                    token.p11.C_VerifyFinal(session.id(), signature);
                } else {
                    byte[] digest;
                    if (type == T_DIGEST) {
                        digest = md.digest();
                    } else {
                        digest = buffer;
                    }
                    token.p11.C_Verify(session.id(), digest, signature);
                }
            } catch (PKCS11Exception e) {
            }
        }
    }
}
