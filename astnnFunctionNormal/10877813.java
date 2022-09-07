class BackupThread extends Thread {
            public Object getValueAt(int row, int col) {
                PowerSupplyCycler psc = (PowerSupplyCycler) PowerSupplyCyclerV.get(row);
                if (col == 1) {
                    return new Boolean(psc.getActive());
                }
                return psc.getChannelName();
            }
}
