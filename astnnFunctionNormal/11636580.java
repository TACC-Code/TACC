class BackupThread extends Thread {
    private void updateMargin() {
        if (NodoDueManager.hasConnection()) {
            WireData data = selectedWire.getLastData();
            if (data == null || marginSlider.getValue() != data.getMargin()) {
                SerialCommunicator comm = NodoDueManager.getApplication().getSerialCommunicator();
                try {
                    CommandInfo ci = CommandLoader.get(Name.WiredSmittTrigger);
                    comm.send(new NodoCommand(ci, String.valueOf(selectedWire.getChannel()), String.valueOf(marginSlider.getValue())));
                    comm.waitCommand(500, 1500);
                } catch (Exception e) {
                    getListener().showStatusMessage(getResourceString("update_fail.margin", e.getMessage()));
                }
            }
        }
    }
}
