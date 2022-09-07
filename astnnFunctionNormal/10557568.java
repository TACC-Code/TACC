class BackupThread extends Thread {
    public boolean doLogin(final String user, final String passwd) {
        account = user.toUpperCase();
        Sha160 h = new Sha160();
        if (h == null) return false;
        try {
            userName = account.getBytes("UTF-8");
            h.update(userName);
            userHash = h.digest();
            h.reset();
            h.update((account + ":" + passwd.toUpperCase()).getBytes("UTF-8"));
            authHash = h.digest();
        } catch (Exception e) {
            errorStr = e.getMessage();
            return false;
        }
        if (!askChallenge()) return false;
        while (!authOk) {
            if (!readReply()) return false;
        }
        return authOk;
    }
}
