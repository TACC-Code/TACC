class BackupThread extends Thread {
    public static int tryCopyFields(Object source, Object target, String[] fieldNames) {
        Map<String, Object> readers = getReaders(source, fieldNames);
        Map<String, Object> writers = getWriters(target, fieldNames);
        return tryCopyFields(source, target, readers, writers);
    }
}
