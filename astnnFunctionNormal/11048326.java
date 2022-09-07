class BackupThread extends Thread {
    public static Channel listentoChannel(int id, Member member) {
        Channel cn = channelhandler.getChannel(id);
        cn.addMember(member);
        return cn;
    }
}
