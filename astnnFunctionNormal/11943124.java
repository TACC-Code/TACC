class BackupThread extends Thread {
    public ReturnValue<IRCJoinBean> getJoin(IRCChannelBean channel, IRCUserBean user) {
        ReturnValue<IRCJoinBean> retval = new ReturnValue<IRCJoinBean>();
        if (!setFlags(retval, NULL, flag(channel == null, CHANNEL, channel.getChannelname()), flag(user == null, USERID, user._getID(), user.getNickname()))) {
            IRCJoinBean asdf = IRCJoinBean.findJoinByUserAndChannel(em, user, channel);
            if (asdf == null) setNotFoundFlag(retval, JOIN, user._getID(), user.getNickname(), channel.getChannelname()); else {
                retval.setValue(asdf);
            }
        }
        return retval;
    }
}
