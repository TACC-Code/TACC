class BackupThread extends Thread {
    @Override
    public void actionPerformed(ActionEvent e) {
        ChannelDialog.getChannelDialog().setVisible(true);
    }
}
