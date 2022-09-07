class BackupThread extends Thread {
    public ReturnValue<IRCJoinBean> joinUser(IRCUserBean user, IRCChannelBean channel, String modeline) {
        ReturnValue<IRCJoinBean> retval = new ReturnValue<IRCJoinBean>();
        if (!setFlags(retval, NULL, flag(user == null, USERNAME), flag(channel == null, CHANNEL))) {
            user = resolve(em, user);
            channel = resolve(em, channel);
            if (!setFlags(retval, NOTFOUND, flag(user == null, USERNAME), flag(channel == null, CHANNEL))) {
                IRCJoinBean newObj = new IRCJoinBean(channel, user);
                newObj.setModeline(modeline);
                IRCJoinBean oldObj = IRCJoinBean.findJoinByUserAndChannel(em, user, channel);
                if (oldObj == null) {
                    try {
                        em.persist(newObj);
                        retval.setValue(newObj);
                    } catch (ConstraintViolationException e) {
                        retval.addMessage(ALREADY_EXISTS, JOIN, user.getNickname(), user.getId() + "", channel.getChannelname());
                    }
                } else {
                    retval.addMessage(ALREADY_EXISTS, JOIN);
                }
            }
        }
        return retval;
    }
}
