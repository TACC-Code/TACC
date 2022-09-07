class BackupThread extends Thread {
    protected byte[] engineSign() throws SignatureException {
        ensureInitialized();
        try {
            byte[] signature;
            if (type == T_UPDATE) {
                int len = keyAlgorithm.equals("DSA") ? 40 : 0;
                signature = token.p11.C_SignFinal(session.id(), len);
            } else {
                byte[] digest;
                if (type == T_DIGEST) {
                    digest = md.digest();
                } else {
                    if (mechanism == CKM_DSA) {
                        if (bytesProcessed != buffer.length) {
                            throw new SignatureException("Data for RawDSA must be exactly 20 bytes long");
                        }
                        digest = buffer;
                    } else {
                        if (bytesProcessed > buffer.length) {
                            throw new SignatureException("Data for NONEwithECDSA" + " must be at most " + RAW_ECDSA_MAX + " bytes long");
                        }
                        digest = new byte[bytesProcessed];
                        System.arraycopy(buffer, 0, digest, 0, bytesProcessed);
                    }
                }
                if (keyAlgorithm.equals("RSA") == false) {
                    signature = token.p11.C_Sign(session.id(), digest);
                } else {
                    byte[] data = encodeSignature(digest);
                    if (mechanism == CKM_RSA_X_509) {
                        data = pkcs1Pad(data);
                    }
                    signature = token.p11.C_Sign(session.id(), data);
                }
            }
            if (keyAlgorithm.equals("RSA") == false) {
                return dsaToASN1(signature);
            } else {
                return signature;
            }
        } catch (PKCS11Exception e) {
            throw new ProviderException(e);
        } finally {
            initialized = false;
            session = token.releaseSession(session);
        }
    }
}
