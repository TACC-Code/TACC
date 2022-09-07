class BackupThread extends Thread {
    public boolean selfTest() {
        OMAC mac = new OMAC(CipherFactory.getInstance(Registry.AES_CIPHER));
        mac.reset();
        Map attr = new HashMap();
        attr.put(MAC_KEY_MATERIAL, KEY0);
        byte[] digest = null;
        try {
            mac.init(attr);
            digest = mac.digest();
        } catch (Exception x) {
            return false;
        }
        if (digest == null) return false;
        return Arrays.equals(DIGEST0, digest);
    }
}
