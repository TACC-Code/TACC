class BackupThread extends Thread {
            public void actionPerformed(ActionEvent e) {
                spScanPV_RB_Text.setText(null);
                spScanPV_RB_Text.setText(mScanPV_RB_Name_Text.getText());
                Channel ch = null;
                if (mScanPV_RB_Name_Text.getText().length() > 0) {
                    ch = ChannelFactory.defaultFactory().getChannel(mScanPV_RB_Name_Text.getText());
                }
                try {
                    if (ch != null && !ch.readAccess()) {
                        String msg = "Read Back PV for scanning:" + System.getProperty("line.separator");
                        msg = msg + "PV (" + ch.channelName() + ") does not have read access. Set the new PV name.";
                        Toolkit.getDefaultToolkit().beep();
                        showInfoPanel(msg, MAIN_PANEL);
                        return;
                    }
                    if (spScanPV_RB_Text.getText().length() > 0) {
                        independValue.setChannelNameRB(spScanPV_RB_Text.getText());
                    } else {
                        independValue.setChannelNameRB(null);
                    }
                } catch (ConnectionException expt) {
                    String msg = "Cannot connect to PVs:" + System.getProperty("line.separator");
                    msg = msg + mScanPV_RB_Name_Text.getText() + System.getProperty("line.separator");
                    msg = msg + "Please, check EPICS or names of PVs.";
                    Toolkit.getDefaultToolkit().beep();
                    showInfoPanel(msg, MAIN_PANEL);
                }
            }
}
