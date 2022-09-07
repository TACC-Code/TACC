class BackupThread extends Thread {
    public PropertyDescriptor(String propertyName, Class<?> beanClass, String getterName, String setterName) throws IntrospectionException {
        super();
        if (beanClass == null) {
            throw new IntrospectionException(Messages.getString("beans.03"));
        }
        if (propertyName == null || propertyName.length() == 0) {
            throw new IntrospectionException(Messages.getString("beans.04"));
        }
        this.setName(propertyName);
        if (getterName != null) {
            if (getterName.length() == 0) {
                throw new IntrospectionException("read or write method cannot be empty.");
            }
            try {
                setReadMethod(beanClass, getterName);
            } catch (IntrospectionException e) {
                setReadMethod(beanClass, createDefaultMethodName(propertyName, "get"));
            }
        }
        if (setterName != null) {
            if (setterName.length() == 0) {
                throw new IntrospectionException("read or write method cannot be empty.");
            }
            setWriteMethod(beanClass, setterName);
        }
    }
}
