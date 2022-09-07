class BackupThread extends Thread {
    public SysConfig(String id, String value, String readPerm, String writePerm) {
        this.id = id;
        this.value = value;
        this.readPerm = readPerm;
        this.writePerm = writePerm;
    }
}
