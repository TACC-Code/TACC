class BackupThread extends Thread {
    public List<String> getChannelTpls(String sysType) {
        return getTpls(sysType, TPL_DEF_CHANNEL);
    }
}
