class BackupThread extends Thread {
    protected final BeanProperty getBeanProperty(Object instance, String propertyName) {
        Class c = instance.getClass();
        Map props;
        if (descriptor == null && cacheProperties) {
            props = getBeanProperties(instance);
            return props == null ? null : (BeanProperty) props.get(propertyName);
        }
        PropertyDescriptorCacheEntry pce = getPropertyDescriptorCacheEntry(c);
        if (pce == null) return null;
        Object pType = pce.propertiesByName.get(propertyName);
        if (pType == null) return null;
        List excludes = null;
        if (descriptor != null) {
            excludes = descriptor.getExcludesForInstance(instance);
            if (excludes == null) excludes = descriptor.getExcludes();
        }
        if (pType instanceof PropertyDescriptor) {
            PropertyDescriptor pd = (PropertyDescriptor) pType;
            Method readMethod = pd.getReadMethod();
            Method writeMethod = pd.getWriteMethod();
            if (readMethod != null && isPublicAccessor(readMethod.getModifiers()) && !includeReadOnly && writeMethod == null) return null;
            if ((excludes != null && excludes.contains(propertyName)) || isPropertyIgnored(c, propertyName)) return null;
            return new BeanProperty(propertyName, pd.getPropertyType(), readMethod, writeMethod, null);
        } else if (pType instanceof Field) {
            Field field = (Field) pType;
            String pName = field.getName();
            int modifiers = field.getModifiers();
            if (isPublicField(modifiers) && pName.equals(propertyName)) {
                return ((excludes != null && excludes.contains(propertyName)) || isPropertyIgnored(c, propertyName)) ? null : new BeanProperty(propertyName, field.getType(), null, null, field);
            }
        }
        return null;
    }
}
