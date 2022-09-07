class BackupThread extends Thread {
    public static Channel listentoChannel(String channel, Member member) {
        Channel cn = channelhandler.getChannel(channel);
        cn.addMember(member);
        return cn;
    }
}
