class BackupThread extends Thread {
    public void loadOptions() {
        System.out.println("loadOptions() started");
        try {
            RecordStore store = RecordStore.openRecordStore("STORE", true);
            if (store.getNumRecords() > 0) {
                byte[] data = store.getRecord(1);
                try {
                    ByteArrayInputStream bais = new ByteArrayInputStream(data);
                    DataInputStream dis = new DataInputStream(bais);
                    SuDokuManager.soundOn = dis.readBoolean();
                    readCurrentPuzzle(dis);
                    SuDokuManager.overwriteOn = dis.readBoolean();
                } catch (IOException ioe) {
                    System.out.println("loadOptions() IOException " + ioe);
                    return;
                }
            } else {
                System.out.println("loadOptions() Store empty");
            }
            store.closeRecordStore();
        } catch (RecordStoreException rse) {
            System.out.println("loadOptions() recordStoreException:" + rse);
        }
        System.out.println("loadOptions() complete");
    }
}
