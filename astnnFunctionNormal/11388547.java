class BackupThread extends Thread {
    @Override
    public void savePreferredChannels(Set<String> channels) {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            PreferredChannels preferredChannels = getPreferredChannels(user, pm);
            if (preferredChannels == null) {
                preferredChannels = new PreferredChannels(user);
            }
            Set<String> currentChannels = preferredChannels.getChannels();
            if (currentChannels != null) {
                currentChannels.clear();
            } else {
                currentChannels = new HashSet<String>();
                preferredChannels.setChannels(currentChannels);
            }
            currentChannels.addAll(channels);
            pm.makePersistent(preferredChannels);
        } finally {
            pm.close();
        }
    }
}
