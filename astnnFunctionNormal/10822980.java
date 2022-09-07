class BackupThread extends Thread {
            public void actionPerformed(ActionEvent e) {
                spMeasurePV_Text.setText(null);
                spMeasurePV_Text.setText(mMeasurePV_Name_Text.getText());
                Channel ch = null;
                if (mMeasurePV_Name_Text.getText().length() > 0) {
                    ch = ChannelFactory.defaultFactory().getChannel(mMeasurePV_Name_Text.getText());
                } else {
                    showInfoPanel("PV for measuring should be specified (Y axis).", MAIN_PANEL);
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }
                try {
                    if (ch != null && !ch.readAccess()) {
                        String msg = "PV for scanning:" + System.getProperty("line.separator");
                        msg = msg + "PV (" + ch.channelName() + ") does not have read access. Set the new PV name.";
                        Toolkit.getDefaultToolkit().beep();
                        showInfoPanel(msg, MAIN_PANEL);
                        return;
                    }
                    if (spMeasurePV_Text.getText().length() > 0) {
                        measuredValue.setChannelName(spMeasurePV_Text.getText());
                    } else {
                        measuredValue.setChannelName(null);
                    }
                } catch (ConnectionException expt) {
                    String msg = "Cannot connect to PVs:" + System.getProperty("line.separator");
                    msg = msg + mMeasurePV_Name_Text.getText() + System.getProperty("line.separator");
                    msg = msg + "Please, check EPICS or names of PVs.";
                    Toolkit.getDefaultToolkit().beep();
                    showInfoPanel(msg, MAIN_PANEL);
                }
            }
}
