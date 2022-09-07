class BackupThread extends Thread {
    private void updatePV_ValuesOnMainPanel() {
        if (independValue.getChannelName() != null) {
            mScannedPV_Val_Label.setText(mScannedPVFormat.format(independValue.getValue()));
        }
        if (independValue.getChannelNameRB() != null) {
            mScannedPV_RB_Val_Label.setText(mScannedPVFormat.format(independValue.getCurrentValueRB()));
        }
        if (measuredValue.getChannelName() != null) {
            mMeasured_Val_Label.setText(mMeasuredPVFormat.format(measuredValue.getInstantValue()));
        }
        if (valuatorValue.getChannelName() != null) {
            valuatorValue.validate();
            mValidationPV_Val_Label.setText(mValidationPVFormat.format(valuatorValue.getValue()));
        }
    }
}
