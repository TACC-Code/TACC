class BackupThread extends Thread {
    public JFieldProperty(Field field, JMethod readMethod, JMethod writeMethod) {
        super(field);
        this.readMethod = readMethod;
        this.writeMethod = writeMethod;
        this.identifier = (field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(EmbeddedId.class) || (readMethod != null && (readMethod.getMember().isAnnotationPresent(Id.class) || readMethod.getMember().isAnnotationPresent(EmbeddedId.class))) || (writeMethod != null && (writeMethod.getMember().isAnnotationPresent(Id.class) || writeMethod.getMember().isAnnotationPresent(EmbeddedId.class))));
    }
}
