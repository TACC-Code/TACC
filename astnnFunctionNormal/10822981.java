class BackupThread extends Thread {
            public void actionPerformed(ActionEvent e) {
                spValidationPV_Text.setText(null);
                spValidationPV_Text.setText(mValidationPV_Name_Text.getText());
                Channel ch = null;
                if (mValidationPV_Name_Text.getText().length() > 0) {
                    ch = ChannelFactory.defaultFactory().getChannel(mValidationPV_Name_Text.getText());
                }
                try {
                    if (ch != null && !ch.readAccess()) {
                        String msg = "PV for scanning:" + System.getProperty("line.separator");
                        msg = msg + "PV (" + ch.channelName() + ") does not have read access. Set the new PV name.";
                        Toolkit.getDefaultToolkit().beep();
                        showInfoPanel(msg, MAIN_PANEL);
                        return;
                    }
                    if (spValidationPV_Text.getText().length() > 0) {
                        valuatorValue.setChannelName(spValidationPV_Text.getText());
                    } else {
                        valuatorValue.setChannelName(null);
                    }
                } catch (ConnectionException expt) {
                    String msg = "Cannot connect to PVs:" + System.getProperty("line.separator");
                    msg = msg + mValidationPV_Name_Text.getText() + System.getProperty("line.separator");
                    msg = msg + "Please, check EPICS or names of PVs.";
                    Toolkit.getDefaultToolkit().beep();
                    showInfoPanel(msg, MAIN_PANEL);
                }
            }
}
