class BackupThread extends Thread {
    private void setLabelStatus() {
        if (getChannel().isOp(Connection.getUserInfo()) || getChannel().isOwner(Connection.getUserInfo())) {
            lblChannelStatus.setText("You are operator, you can edit settings!");
            lblChannelStatus.setBackground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
            setIsOperator(true);
        } else {
            lblChannelStatus.setText("You are NOT operator, you can't edit settings!");
            lblChannelStatus.setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
            setIsOperator(false);
        }
    }
}
