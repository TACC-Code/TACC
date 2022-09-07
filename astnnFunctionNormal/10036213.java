class BackupThread extends Thread {
    public String getHashCalculado(String w_ac) {
        try {
            String wmd = Util.digest(w_ac, "ISO8859_1");
            return wmd;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
