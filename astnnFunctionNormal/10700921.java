class BackupThread extends Thread {
    private void validateResponse(String response) throws SaslException {
        byte[] responseHash;
        if (response.startsWith(WORD + COLON)) {
            response = response.substring(WORD.length());
            long l = OTPDictionary.convertWordsToHash(response);
            responseHash = OTPUtil.convertHexToBytes(response);
            cat.debug("responseHash: " + responseHash);
        } else if (response.startsWith(HEX + COLON)) {
            response = response.substring(HEX.length());
            responseHash = OTPUtil.convertHexToBytes(response);
            cat.debug("responseHash: " + responseHash);
        } else throw new SaslException("Unknown format");
        byte[] hashResponseHash = otp.digest(responseHash);
        cat.debug("hash(responseHash): " + SaslUtil.dumpString(hashResponseHash));
        cat.debug("lastHash          : " + SaslUtil.dumpString(lastHash));
        if (!SaslUtil.areEqual(this.lastHash, hashResponseHash)) throw new SaslException("validateResponse: " + ERR_AUTH_FAILURE);
        try {
            String sequence = (String) credentials.get(SEGNUM_FIELD);
            int seqnum = Integer.parseInt(sequence);
            seqnum--;
            credentials.put(SEGNUM_FIELD, new Integer(seqnum).toString());
            credentials.put(LAST_HASH_FIELD, SaslUtil.tob64(responseHash));
            authenticator.update(credentials);
        } catch (Exception x) {
            if (x instanceof SaslException) throw (SaslException) x;
            throw new SaslException("validateResponse()", x);
        }
    }
}
