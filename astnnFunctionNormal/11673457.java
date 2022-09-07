class BackupThread extends Thread {
    public LinkedList<String> getChannelNames() {
        LinkedList<String> chNames = new LinkedList<String>();
        for (int i = 0; i < columns.size(); i++) {
            TableConfigColumn aCol = (TableConfigColumn) columns.get(i);
            chNames.add(aCol.getChannelMapping());
        }
        return chNames;
    }
}
