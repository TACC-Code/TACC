class BackupThread extends Thread {
    protected void insert(String key, Object value, boolean overwrite) {
        if (key == null) throw new RuntimeException("Error in the internal method insert() of class " + Utilities.getClassName(this) + ": adding a property with a null key  is not allowed.");
        if (value == null) throw new RuntimeException("Error in the internal method insert() of class " + Utilities.getClassName(this) + ": adding a property with a null value is not allowed.");
        if (!overwrite && this.hashtable.containsKey(key)) throw new RuntimeException("Error in the internal method insert() of class " + Utilities.getClassName(this) + ": a property with key '" + key + "' is already defined in this property list.");
        IMetadataEntry metadtaEntry = new MetadataEntry(key);
        metadtaEntry.setValue(value);
        Vector entries = (Vector) this.hashtable.get(key);
        if (entries == null) entries = new Vector();
        entries.add(metadtaEntry);
        this.hashtable.put(key, entries);
    }
}
