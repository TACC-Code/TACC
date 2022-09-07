class BackupThread extends Thread {
    protected boolean engineVerify(byte[] signature) throws SignatureException {
        ensureInitialized();
        try {
            if (keyAlgorithm.equals("DSA")) {
                signature = asn1ToDSA(signature);
            } else if (keyAlgorithm.equals("EC")) {
                signature = asn1ToECDSA(signature);
            }
            if (type == T_UPDATE) {
                token.p11.C_VerifyFinal(session.id(), signature);
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
                    token.p11.C_Verify(session.id(), digest, signature);
                } else {
                    byte[] data = encodeSignature(digest);
                    if (mechanism == CKM_RSA_X_509) {
                        data = pkcs1Pad(data);
                    }
                    token.p11.C_Verify(session.id(), data, signature);
                }
            }
            return true;
        } catch (PKCS11Exception e) {
            long errorCode = e.getErrorCode();
            if (errorCode == CKR_SIGNATURE_INVALID) {
                return false;
            }
            if (errorCode == CKR_SIGNATURE_LEN_RANGE) {
                return false;
            }
            if (errorCode == CKR_DATA_LEN_RANGE) {
                return false;
            }
            throw new ProviderException(e);
        } finally {
            initialized = false;
            session = token.releaseSession(session);
        }
    }
}
