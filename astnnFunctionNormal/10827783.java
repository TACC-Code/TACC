class BackupThread extends Thread {
    public boolean selfTest() {
        if (valid == null) {
            String d = Util.toString(new Haval().digest());
            valid = Boolean.valueOf(DIGEST0.equals(d));
        }
        return valid.booleanValue();
    }
}
