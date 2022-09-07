class BackupThread extends Thread {
    protected static String getKey(Serializable pObject) throws Exception {
        String vRetour = "";
        try {
            ObjectOutputStream vOOS;
            ByteArrayOutputStream vBOS = new ByteArrayOutputStream();
            vOOS = new ObjectOutputStream(vBOS);
            vOOS.writeObject(pObject);
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            byte[] vEncodedBytes = digest.digest(vBOS.toByteArray());
            vRetour = toBase64(vEncodedBytes).replace('/', '_');
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("generating key for object [" + pObject + "] : key = [" + vRetour + "]");
            }
        } catch (IOException e) {
            System.err.println("erreur getKey : " + e);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("erreur getKey : " + e);
        }
        return vRetour;
    }
}
