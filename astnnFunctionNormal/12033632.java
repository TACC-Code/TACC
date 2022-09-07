class BackupThread extends Thread {
    public boolean selfTest() {
        if (valid == null) {
            Sha0 md = new Sha0();
            md.update((byte) 0x61);
            md.update((byte) 0x62);
            md.update((byte) 0x63);
            String result = Util.toString(md.digest());
            valid = new Boolean(DIGEST0.equals(result));
        }
        return valid.booleanValue();
    }
}
