class BackupThread extends Thread {
    private void getDetails() {
        if (favourite.getTitleString() != null) {
            txtTitle.setText(favourite.getTitleString());
            cmbTitle.setSelectedItem(Application.getInstance().getLocalizedMessage("exactly"));
        } else if (favourite.getTitleContains() != null) {
            txtTitle.setText(favourite.getTitleContains());
            cmbTitle.setSelectedItem(Application.getInstance().getLocalizedMessage("contains"));
        } else if (favourite.getTitleRegex() != null) {
            txtTitle.setText(favourite.getTitleRegex());
            cmbTitle.setSelectedItem(Application.getInstance().getLocalizedMessage("regular_expression"));
        }
        if (favourite.getChannelID() != null) {
            TVChannelsSet.Channel ch = (TVChannelsSet.Channel) channels.get(favourite.getChannelID());
            if (ch != null) {
                cmbChannel.setSelectedItem(ch);
            }
        }
        if (!favourite.getAfterTime().isEmpty()) {
            txtAfter.setText(favourite.getAfterTime().getHHMMString());
        }
        if (!favourite.getBeforeTime().isEmpty()) {
            txtBefore.setText(favourite.getBeforeTime().getHHMMString());
        }
        if (favourite.getDayOfWeek() != -1) {
            cmbDayOfWeek.setSelectedIndex(favourite.getDayOfWeek());
        }
        calcTxtName();
    }
}
