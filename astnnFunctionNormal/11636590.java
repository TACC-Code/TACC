class BackupThread extends Thread {
    @Action
    public void wireTickAction() {
        CommandInfo ci = null;
        String val = null;
        String idx = null;
        if (selectedWire != null) {
            idx = "" + selectedWire.getChannel();
            if (selectedWire.getType() == Type.Input) {
                ci = CommandLoader.get(Name.WiredPullup);
                val = wireCheck.isSelected() ? "On" : "Off";
            } else {
                ci = CommandLoader.get(Name.WiredOut);
                val = wireCheck.isSelected() ? "On" : "Off";
            }
        }
        if (NodoDueManager.hasConnection() == false) {
            return;
        }
        SerialCommunicator comm = NodoDueManager.getApplication().getSerialCommunicator();
        try {
            comm.send(new NodoCommand(ci, idx, val));
            comm.waitCommand(500, 1000);
        } catch (Exception e) {
            getListener().showStatusMessage(getResourceString("update_fail.wireChange", e.getMessage()));
        }
    }
}
