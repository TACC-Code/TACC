class BackupThread extends Thread {
    public AccountSession createAccountSession(String userName, String password) {
        Account account = accountDao.findByUsername(userName);
        if (account == null) {
            log.info("Username does not exist: " + userName);
            return null;
        }
        String hash = bytes2String(sha1.digest((password + "-" + account.getPasswordsalt()).getBytes()));
        if (!account.getPasswordsha1().equals(hash)) {
            log.info("Invalid credentials supplied for : " + userName);
            return null;
        }
        String token = generateToken();
        AccountSession session = new AccountSession();
        session.setTokenCreation(new Timestamp(System.currentTimeMillis()));
        session.setToken(token);
        accountSessionDao.saveOrUpdate(account.getAccountId(), session);
        return session;
    }
}
