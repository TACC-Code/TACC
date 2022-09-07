class BackupThread extends Thread {
    public Object getValueAt(int row, int column) {
        if (_search != null) {
            switch(column) {
                case 0:
                    return new GuiObject(_search.getChannelAt(row).getName(), _search.getChannelAt(row), IconManager.getIcon("Users"));
                case 1:
                    return new Integer(_search.getChannelAt(row).getUserCount());
                case 2:
                    return _search.getChannelAt(row).getTopic();
                default:
                    return new String("");
            }
        } else return null;
    }
}
