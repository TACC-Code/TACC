class BackupThread extends Thread {
    public PVLoggerDataSource(long id) {
        ConnectionDictionary dict = ConnectionDictionary.defaultDictionary();
        SqlStateStore store;
        if (dict != null) {
            store = new SqlStateStore(dict);
        } else {
            ConnectionPreferenceController.displayPathPreferenceSelector();
            dict = ConnectionDictionary.defaultDictionary();
            store = new SqlStateStore(dict);
        }
        mss = store.fetchMachineSnapshot(id);
        css = mss.getChannelSnapshots();
        qPVMap = getQuadMap();
        qPSPVMap = getQuadPSMap();
    }
}
