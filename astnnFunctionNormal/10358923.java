class BackupThread extends Thread {
    private void editChannelName() {
        boolean finished = false;
        String originalName = channel.getName();
        SubChannelOutComboBoxModel model = (SubChannelOutComboBoxModel) getChannelOutModel();
        ChannelList subChannels = model.getChannels();
        while (!finished) {
            String retVal = JOptionPane.showInputDialog(this, "Please Enter SubChannel Name", originalName);
            if (retVal != null && retVal.trim().length() > 0 && !retVal.equals(originalName)) {
                retVal = retVal.trim();
                if (!isValidChannelName(retVal)) {
                    JOptionPane.showMessageDialog(this, "Error: Channel names may only contain letters, " + "numbers, or underscores", BlueSystem.getString("common.error"), JOptionPane.ERROR_MESSAGE);
                    finished = false;
                } else if (retVal.equals(Channel.MASTER) || subChannels.isChannelNameInUse(retVal)) {
                    JOptionPane.showMessageDialog(this, "Error: Channel Name already in use", BlueSystem.getString("common.error"), JOptionPane.ERROR_MESSAGE);
                    finished = false;
                } else {
                    channel.setName(retVal);
                    finished = true;
                }
            } else {
                finished = true;
            }
        }
    }
}
