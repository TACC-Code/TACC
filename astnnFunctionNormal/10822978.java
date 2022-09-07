class BackupThread extends Thread {
            public void actionPerformed(ActionEvent e) {
                spScanPV_Text.setText(null);
                spScanPV_Text.setText(mScanPV_Name_Text.getText());
                Channel ch = null;
                if (mScanPV_Name_Text.getText().length() > 0) {
                    ch = ChannelFactory.defaultFactory().getChannel(mScanPV_Name_Text.getText());
                } else {
                    showInfoPanel("PV for scanning should be specified (X axis).", MAIN_PANEL);
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }
                try {
                    if (ch != null && !ch.writeAccess()) {
                        String msg = "PV for scanning:" + System.getProperty("line.separator");
                        msg = msg + "PV (" + ch.channelName() + ") does not have write access. Set the new PV name.";
                        Toolkit.getDefaultToolkit().beep();
                        showInfoPanel(msg, MAIN_PANEL);
                        return;
                    }
                    if (spScanPV_Text.getText().length() > 0) {
                        independValue.setChannelName(spScanPV_Text.getText());
                    } else {
                        independValue.setChannelName(null);
                    }
                } catch (ConnectionException expt) {
                    String msg = "Cannot connect to PVs:" + System.getProperty("line.separator");
                    msg = msg + mScanPV_Name_Text.getText() + System.getProperty("line.separator");
                    msg = msg + "Please, check EPICS or names of PVs.";
                    Toolkit.getDefaultToolkit().beep();
                    showInfoPanel(msg, MAIN_PANEL);
                }
            }
}
