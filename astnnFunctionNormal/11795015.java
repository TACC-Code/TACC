class BackupThread extends Thread {
        public String getChannelKey(User user) throws ObjectNotFoundException {
            if (checkPerms(user) && _chanKey != null) {
                return _chanKey;
            }
            throw new ObjectNotFoundException("No Permissions");
        }
}
