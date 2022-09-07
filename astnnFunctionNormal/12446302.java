class BackupThread extends Thread {
    protected JComponent createChannelPane() {
        if (channelPane == null) {
            channelView = new ChannelTableView(getChannelData());
            channelView.setSelectionChangedExecutor(new AbstractAction() {

                private static final long serialVersionUID = 713L;

                @Override
                public void actionPerformed(ActionEvent ae) {
                    channelChanged(ae);
                }
            });
            channelView.setPopDoubleLeftExecutor(new AbstractAction() {

                private static final long serialVersionUID = 713L;

                @Override
                public void actionPerformed(ActionEvent ae) {
                    popChannelDoubleLeft(ae);
                }
            });
            channelView.setPopSingleRightExecutor(new AbstractAction() {

                private static final long serialVersionUID = 713L;

                @Override
                public void actionPerformed(ActionEvent ae) {
                    popChannelSingleRight(ae);
                }
            });
            channelPane = new ApplicationPage(getName() + ".channel", channelView.getView());
        }
        return channelPane;
    }
}
