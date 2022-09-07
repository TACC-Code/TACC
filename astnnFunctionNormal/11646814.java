class BackupThread extends Thread {
    private void init() {
        deviceComboBox.setModel(new BaseComboBoxModel<String>(true, DevicePool.instance().getMidiDeviceNames(Direction.IN)));
        deviceComboBox.setSelectedItem(keyboard.getInput());
        try {
            channelSpinner.setValue(keyboard.getChannel() + 1);
        } catch (ProcessingException e) {
            channelSpinner.setEnabled(false);
        }
        try {
            fromSpinner.setValue(keyboard.getFrom());
            toSpinner.setValue(keyboard.getTo());
            transposeSpinner.setValue(keyboard.getTranspose());
        } catch (ProcessingException e) {
            fromSpinner.setEnabled(false);
            toSpinner.setEnabled(false);
            transposeSpinner.setEnabled(false);
        }
    }
}
