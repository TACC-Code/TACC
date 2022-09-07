class BackupThread extends Thread {
    PropertyAdapterImpl(ClassPropertyAdapter classAdapter, String name, Class type, Method readMethod, Method writeMethod) {
        this.classAdapter = classAdapter;
        this.name = name;
        this.type = type;
        this.readMethod = readMethod;
        this.writeMethod = writeMethod;
        castRequired = readMethod != null && readMethod.getReturnType() != type;
    }
}
