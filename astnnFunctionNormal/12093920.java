class BackupThread extends Thread {
    public boolean selfTest() {
        if (valid == null) {
            valid = new Boolean(DIGEST0.equals(Util.toString(new Tiger128().digest())));
        }
        return valid.booleanValue();
    }
}
