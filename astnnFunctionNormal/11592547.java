class BackupThread extends Thread {
    public MetadataAttributeDescriptor(String name, Method readMethod, Method writeMethod) {
        if (readMethod == null) throw new IllegalArgumentException("readMethod must not be null");
        this.name = name;
        this.readMethod = readMethod;
        this.writeMethod = writeMethod;
    }
}
