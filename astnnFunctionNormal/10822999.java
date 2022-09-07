class BackupThread extends Thread {
    private boolean checkConnections() {
        Channel chIndp = null;
        Channel chIndp_RB = null;
        Channel chMeasure = null;
        Channel chValidator = null;
        if (spScanPV_Text.getText().length() > 0) {
            chIndp = ChannelFactory.defaultFactory().getChannel(spScanPV_Text.getText());
        } else {
            showInfoPanel("PV for scanning should be specified (X axis).", PV_SELECT_PANEL);
            return false;
        }
        if (spMeasurePV_Text.getText().length() > 0) {
            chMeasure = ChannelFactory.defaultFactory().getChannel(spMeasurePV_Text.getText());
        } else {
            showInfoPanel("PV for measuring should be specified (Y axis).", PV_SELECT_PANEL);
            return false;
        }
        if (spScanPV_RB_Text.getText().length() > 0) {
            chIndp_RB = ChannelFactory.defaultFactory().getChannel(spScanPV_RB_Text.getText());
        }
        if (spValidationPV_Text.getText().length() > 0) {
            chValidator = ChannelFactory.defaultFactory().getChannel(spValidationPV_Text.getText());
        }
        try {
            if (chIndp != null && !chIndp.writeAccess()) {
                String msg = "PV for scanning:" + System.getProperty("line.separator");
                msg = msg + "PV (" + chIndp.channelName() + ") does not have write access. Set the new PV name.";
                showInfoPanel(msg, PV_SELECT_PANEL);
                return false;
            }
            if (chIndp_RB != null && !chIndp_RB.readAccess()) {
                String msg = "PV Read Back for scanning:" + System.getProperty("line.separator");
                msg = msg + "PV (" + chIndp.channelName() + ") does not have read access. Set the new PV name.";
                showInfoPanel(msg, PV_SELECT_PANEL);
                return false;
            }
            if (chMeasure != null && !chMeasure.readAccess()) {
                String msg = "PV for measuring:" + System.getProperty("line.separator");
                msg = msg + "PV (" + chIndp.channelName() + ") does not have read access. Set the new PV name.";
                showInfoPanel(msg, PV_SELECT_PANEL);
                return false;
            }
            if (chValidator != null && !chValidator.readAccess()) {
                String msg = "PV for valuation:" + System.getProperty("line.separator");
                msg = msg + "PV (" + chIndp.channelName() + ") does not have read access. Set the new PV name.";
                showInfoPanel(msg, PV_SELECT_PANEL);
                return false;
            }
        } catch (ConnectionException e) {
            String msg = "Cannot connect to PVs:" + System.getProperty("line.separator");
            if (spScanPV_Text.getText().length() > 0) {
                msg = msg + spScanPV_Text.getText() + System.getProperty("line.separator");
            }
            if (spScanPV_RB_Text.getText().length() > 0) {
                msg = msg + spScanPV_RB_Text.getText() + System.getProperty("line.separator");
            }
            if (spMeasurePV_Text.getText().length() > 0) {
                msg = msg + spMeasurePV_Text.getText() + System.getProperty("line.separator");
            }
            if (spValidationPV_Text.getText().length() > 0) {
                msg = msg + spValidationPV_Text.getText() + System.getProperty("line.separator");
            }
            msg = msg + "Please, check EPICS or names of PVs.";
            showInfoPanel(msg, PV_SELECT_PANEL);
            return false;
        }
        return true;
    }
}
