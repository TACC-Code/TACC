class BackupThread extends Thread {
    public ReturnValue partUser(IRCUserBean user, IRCChannelBean channel) {
        ReturnValue retval = new ReturnValue();
        try {
            if (!setFlags(retval, NULL, flag(user == null, USERID, user._getID()), flag(channel == null, CHANNEL, channel.getChannelname()))) {
                user = resolve(em, user);
                channel = resolve(em, channel);
                if (!setFlags(retval, NOTFOUND, flag(user == null, USERID, user.getNickname(), user), flag(channel == null, CHANNEL, channel))) {
                    try {
                        ReturnValue<IRCJoinBean> joinRV = getJoin(channel, user);
                        List<IRCJoinBean> currentjoines = channel.getJoins();
                        if (currentjoines.size() == 1) {
                            em.remove(channel);
                            retval.addMessage(REMOVED, CHANNEL);
                        } else {
                            em.remove(em.find(IRCJoinBean.class, joinRV.getValue()._getID()));
                            em.refresh(channel);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return retval;
    }
}
