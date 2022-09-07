class BackupThread extends Thread {
    public void fromDSM2Model(DSM2Model model) {
        List<InputTable> tables = fromChannels(model.getChannels());
        for (InputTable table : tables) {
            replaceTable(table);
        }
        List<InputTable> nodeTables = fromNodes(model.getNodes());
        for (InputTable table : nodeTables) {
            replaceTable(table);
        }
        List<InputTable> reservoirTables = fromReservoirs(model.getReservoirs());
        for (InputTable table : reservoirTables) {
            replaceTable(table);
        }
        List<InputTable> gateTables = fromGates(model.getGates());
        for (InputTable table : gateTables) {
            replaceTable(table);
        }
    }
}
