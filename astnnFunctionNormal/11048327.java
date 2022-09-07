class BackupThread extends Thread {
    public Channel listenToChannel(int id) {
        Channel cn = channelhandler.getChannel(id);
        cn.addMember(this.member);
        return cn;
    }
}
