class BackupThread extends Thread {
    public void testEquals_PropertyType() throws SecurityException, NoSuchMethodException, IntrospectionException {
        String propertyName = "PropertyFour";
        Class<MockJavaBean> beanClass = MockJavaBean.class;
        Method readMethod = beanClass.getMethod("get" + propertyName, (Class[]) null);
        Method writeMethod = beanClass.getMethod("set" + propertyName, new Class[] { String[].class });
        Method indexedReadMethod = beanClass.getMethod("get" + propertyName, new Class[] { Integer.TYPE });
        Method indexedWriteMethod = beanClass.getMethod("set" + propertyName, new Class[] { Integer.TYPE, String.class });
        IndexedPropertyDescriptor ipd = new IndexedPropertyDescriptor(propertyName, readMethod, writeMethod, indexedReadMethod, indexedWriteMethod);
        IndexedPropertyDescriptor ipd2 = new IndexedPropertyDescriptor("PropertySix", beanClass);
        assertFalse(ipd.getPropertyType().equals(ipd2.getPropertyType()));
        assertFalse(ipd.equals(ipd2));
    }
}
