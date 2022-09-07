class BackupThread extends Thread {
    public static byte[] getHash(String algName, byte[] text, boolean derWrapped) {
        try {
            MessageDigest md = MessageDigest.getInstance(algName);
            byte[] data = md.digest(text);
            if (derWrapped) {
                String oid = algName.equals("SHA1") ? PKIService.OID_SHA1 : PKIService.OID_SHA256;
                DigestInfo di = new DigestInfo(new AlgorithmIdentifier(new DERObjectIdentifier(oid)), data);
                return di.getDEREncoded();
            } else {
                return data;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
