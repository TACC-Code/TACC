class BackupThread extends Thread {
    @SuppressWarnings("unchecked")
    public PropertyDesc<?> createPropertyDesc(final Class<?> componentClass, final Method writeMethod, Class<?>[] paramTypes) {
        String propertyName = JavaBeansUtil.getPropertyName(writeMethod.getName());
        final Method readMethod = getReadMethod(componentClass, writeMethod);
        Class<?> propertyType = null;
        if (readMethod != null) {
            propertyType = readMethod.getReturnType();
        } else {
            propertyType = JavaBeansUtil.findPropertyTypeFromWriteMethod(writeMethod);
        }
        PropertyDesc pd = new PropertyDescImpl(componentClass, propertyType, propertyName);
        pd.setWriteMethod(writeMethod);
        if (readMethod != null) {
            pd.setReadMethod(readMethod);
        }
        InjectConfig injectDesc = new PropertyInjectConfig(writeMethod.getParameterTypes());
        pd.addConfig(injectDesc);
        return pd;
    }
}
