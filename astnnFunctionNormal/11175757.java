class BackupThread extends Thread {
    public String getInstallPath() {
        return GameDatabase.getInstallPath(roomData.getChannel());
    }
}
