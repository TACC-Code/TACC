class BackupThread extends Thread {
    public boolean updatePlayerName(String oldname, String newname) {
        boolean found = false;
        for (int i = 0; TabOrganizer.getChannelPanel(i) != null; i++) {
            found = TabOrganizer.getChannelPanel(i).updatePlayerName(oldname, newname) || found;
        }
        if (TabOrganizer.getRoomPanel() != null) {
            found = TabOrganizer.getRoomPanel().updatePlayerName(oldname, newname) || found;
        }
        TabOrganizer.updateTitleOnTab(oldname, newname);
        found = Globals.getContactList().updateName(oldname, newname) || found;
        return found;
    }
}
