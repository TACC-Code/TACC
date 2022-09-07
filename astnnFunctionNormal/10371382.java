class BackupThread extends Thread {
    public void writeDestValue(Object runtimeDestObj, Object destFieldValue) {
        if (log.isDebugEnabled()) {
            log.debug("Getting ready to invoke write method on the destination object.  Dest Obj: " + MappingUtils.getClassNameWithoutPackage(runtimeDestObj.getClass()) + ", Dest value: " + destFieldValue);
        }
        DozerPropertyDescriptorIF propDescriptor = getDestPropertyDescriptor(runtimeDestObj.getClass());
        propDescriptor.setPropertyValue(runtimeDestObj, destFieldValue, this);
    }
}
