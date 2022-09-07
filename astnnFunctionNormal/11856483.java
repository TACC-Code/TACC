class BackupThread extends Thread {
    public AttributeMapper(Attribute attribute, Method readMethod, Method writeMethod) throws OdmException {
        super(readMethod, writeMethod);
        this.attribute = attribute;
    }
}
