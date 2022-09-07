class BackupThread extends Thread {
    public AnnotationValidatingBindingException(final String dtoField, final String dtoBinder, final String dtoBinderClass, final String entityField, final String entityBinder, final String entityBinderClass, final boolean dtoRead) {
        super("Type mismatch is detected for: DTO " + dtoField + (dtoRead ? " read" : " write") + " {" + dtoBinder + "[" + dtoBinderClass + "]} " + "and Entity " + entityField + (dtoRead ? " write" : " read") + " {" + entityBinder + "[" + entityBinderClass + "]}. Consider using a converter.");
        this.dtoBinder = dtoBinder;
        this.dtoBinderClass = dtoBinderClass;
        this.entityBinder = entityBinder;
        this.entityBinderClass = entityBinderClass;
        this.dtoField = dtoField;
        this.entityField = entityField;
        this.dtoRead = dtoRead;
    }
}
