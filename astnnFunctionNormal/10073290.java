class BackupThread extends Thread {
    private void loadData() {
        if (listener == null) return;
        String[][] all = listener.getData(dataTableIndex, "", otherID);
        if (all == null || all.length < 1) return;
        columnNames = all[0];
        data = new String[all.length - 1][];
        for (int i = 0; i < all.length - 1; i++) data[i] = all[i + 1];
        columnFormatter = new DataTableCellFormatter[columnNames.length];
        for (int i = 0; i < columnNames.length; i++) columnFormatter[i] = listener.getColumnFormatter(dataTableIndex, columnNames[i]);
        return;
    }
}
