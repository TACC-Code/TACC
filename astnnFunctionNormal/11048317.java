class BackupThread extends Thread {
    public Channel listenToChannel(String name) {
        Channel cn = channelhandler.getChannel(name);
        cn.addMember(this.member);
        return cn;
    }
}
