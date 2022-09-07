class BackupThread extends Thread {
    public String[] getChannelList() {
        return (String[]) progList.keySet().toArray(new String[0]);
    }
}
