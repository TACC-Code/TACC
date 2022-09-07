class BackupThread extends Thread {
    public void replaceIdent(User user, String fullident) {
        if (fullident == null || fullident.length() == 0) return;
        Collection<User> ret;
        try {
            ret = _gctx.getUserManager().getAllUsers();
        } catch (UserFileException e) {
            logger.debug("There was a problem trying to getAllUsers(): " + e.getMessage());
            logger.debug("Falling back to old behavior and setting new ident on user " + user.getName());
            user.getKeyedMap().setObject(UserManagement.IRCIDENT, fullident);
            return;
        }
        for (Iterator<User> iter = ret.iterator(); iter.hasNext(); ) {
            User u = iter.next();
            String ident = u.getKeyedMap().getObjectString(UserManagement.IRCIDENT);
            if (user.isDeleted() || user.getName().equals(u.getName()) || ident == null || ident.equals("") || ident.equalsIgnoreCase("N/A")) continue;
            if (ident.equalsIgnoreCase(fullident)) {
                logger.debug("Removing ident record from user " + u.getName() + " since it now belongs to " + user.getName());
                u.getKeyedMap().setObject(UserManagement.IRCIDENT, "");
            }
        }
        String botnick = getIRCConnection().getClientState().getNick().getNick();
        String newnick = fullident.substring(0, fullident.indexOf("!"));
        String oldident = user.getKeyedMap().getObjectString(UserManagement.IRCIDENT);
        String oldnick = "";
        if (oldident != null && oldident.length() > 0 && oldident.indexOf("!") != -1) oldnick = oldident.substring(0, oldident.indexOf("!"));
        if (_singlesession && (botnick.equalsIgnoreCase(oldnick) || botnick.equalsIgnoreCase(newnick) || (oldnick.length() > 0 && !oldnick.equalsIgnoreCase(newnick)))) {
            Boolean abort = false;
            Boolean clear = false;
            for (Enumeration e = getIRCConnection().getClientState().getChannels(); e.hasMoreElements(); ) {
                Channel chan = (Channel) e.nextElement();
                if (botnick.equalsIgnoreCase(oldnick)) {
                    abort = true;
                    if (!botnick.equalsIgnoreCase(newnick)) getIRCConnection().getCommandSender().sendCommand(new KickCommand(chan.getName(), newnick, "This is what happens when you try to be smart.")); else clear = true;
                } else if (botnick.equalsIgnoreCase(newnick)) {
                    abort = true;
                    if (!botnick.equalsIgnoreCase(oldnick)) getIRCConnection().getCommandSender().sendCommand(new KickCommand(chan.getName(), oldnick, "This is what happens when you try to be smart.")); else clear = true;
                } else if (!botnick.equalsIgnoreCase(oldnick) && chan.isMemberInChannel(oldnick)) {
                    getIRCConnection().getCommandSender().sendCommand(new KickCommand(chan.getName(), oldnick, "Kicked by: " + newnick + ", only one session per user allowed"));
                }
            }
            if (clear) user.getKeyedMap().setObject(UserManagement.IRCIDENT, "");
            if (abort) return;
        }
        user.getKeyedMap().setObject(UserManagement.IRCIDENT, fullident);
    }
}
