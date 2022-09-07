class BackupThread extends Thread {
    @Override
    public String toString() {
        return "sync=" + sync + "\nnoSync=" + noSync + "\nwriteNoSync=" + writeNoSync + "\ndurability=" + durability + "\nconsistencyPolicy=" + consistencyPolicy + "\nnoWait=" + noWait + "\nreadUncommitted=" + readUncommitted + "\nreadCommitted=" + readCommitted + "\nSerializableIsolation=" + serializableIsolation + "\n";
    }
}
