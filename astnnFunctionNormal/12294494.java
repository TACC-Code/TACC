class BackupThread extends Thread {
    private Attribute<T, Object> createAttribute(Method oneMethod, boolean read, boolean write) {
        String propertyName = ReflectUtils.extractPropertyName(oneMethod);
        boolean isGetter = ReflectUtils.isGetter(oneMethod);
        boolean isSetter = ReflectUtils.isSetter(oneMethod);
        if (!isGetter && !isSetter) {
            return null;
        }
        Method getter = isGetter ? oneMethod : ReflectUtils.findGetter(this.clAna, propertyName);
        Method setter = isSetter ? oneMethod : ReflectUtils.findSetter(this.clAna, propertyName);
        String getterPropName = getUserPropertyValue(getter);
        String setterPropName = getUserPropertyValue(setter);
        boolean unmatch = read && getter == null || write && setter == null || getter != null && getterPropName == null || setter != null && setterPropName == null || setter != null && getter != null && !getterPropName.equals(setterPropName);
        if (unmatch) {
            return null;
        }
        return new PropertyBridge(getter, setter);
    }
}
