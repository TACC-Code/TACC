class BackupThread extends Thread {
    public String encrypt(String s) throws Exception {
        String s2 = s;
        if (doEncrypt) {
            synchronized (md) {
                md.update(s.getBytes(encoding));
                s2 = (new BASE64Encoder()).encode(md.digest());
                md.reset();
            }
        }
        return s2;
    }
}
