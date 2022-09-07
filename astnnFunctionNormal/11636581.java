class BackupThread extends Thread {
    private void updateThreshold() {
        if (NodoDueManager.hasConnection()) {
            WireData data = selectedWire.getLastData();
            if (data == null || thresholdSlider.getValue() != data.getThreshold()) {
                SerialCommunicator comm = NodoDueManager.getApplication().getSerialCommunicator();
                try {
                    CommandInfo ci = CommandLoader.get(Name.WiredThreshold);
                    comm.send(new NodoCommand(ci, String.valueOf(selectedWire.getChannel()), String.valueOf(thresholdSlider.getValue())));
                    comm.waitCommand(500, 1000);
                } catch (Exception e) {
                    getListener().showStatusMessage(getResourceString("update_fail.threshold", e.getMessage()));
                }
            }
        }
    }
}
