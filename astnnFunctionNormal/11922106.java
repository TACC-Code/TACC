class BackupThread extends Thread {
    protected Map getBeanProperties(Object instance) {
        Class c = instance.getClass();
        Map props;
        if (descriptor == null) {
            props = (Map) beanPropertyCache.get(c);
            if (props != null) return props;
        }
        props = new HashMap();
        PropertyDescriptor[] pds = getPropertyDescriptors(c);
        if (pds == null) return null;
        List excludes = null;
        if (descriptor != null) {
            excludes = descriptor.getExcludesForInstance(instance);
            if (excludes == null) excludes = descriptor.getExcludes();
        }
        for (PropertyDescriptor pd : pds) {
            String propertyName = pd.getName();
            Method readMethod = pd.getReadMethod();
            Method writeMethod = pd.getWriteMethod();
            if (readMethod != null && isPublicAccessor(readMethod.getModifiers()) && !includeReadOnly && writeMethod == null) continue;
            if ((excludes != null && excludes.contains(propertyName)) || isPropertyIgnored(c, propertyName)) continue;
            if (includeReadOnly && writeMethod == null && "class".equals(propertyName)) continue;
            final Class<?> type = pd.getPropertyType();
            if (type != null && ClassLoader.class.isAssignableFrom(type)) continue;
            props.put(propertyName, new BeanProperty(propertyName, pd.getPropertyType(), readMethod, writeMethod, null));
        }
        Field[] fields = instance.getClass().getFields();
        for (Field field : fields) {
            String propertyName = field.getName();
            int modifiers = field.getModifiers();
            if (isPublicField(modifiers) && !props.containsKey(propertyName)) {
                if ((excludes != null && excludes.contains(propertyName)) || isPropertyIgnored(c, propertyName)) continue;
                props.put(propertyName, new BeanProperty(propertyName, field.getType(), null, null, field));
            }
        }
        if (descriptor == null && cacheProperties) {
            synchronized (beanPropertyCache) {
                Map props2 = (Map) beanPropertyCache.get(c);
                if (props2 == null) beanPropertyCache.put(c, props); else props = props2;
            }
        }
        return props;
    }
}
