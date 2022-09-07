class BackupThread extends Thread {
    public void testEquals_WriteMethod() throws SecurityException, NoSuchMethodException, IntrospectionException {
        String propertyName = "PropertyFour";
        Class<MockJavaBean> beanClass = MockJavaBean.class;
        Method readMethod = beanClass.getMethod("get" + propertyName, (Class[]) null);
        Method writeMethod = beanClass.getMethod("setPropertyFive", new Class[] { String[].class });
        Method indexedReadMethod = beanClass.getMethod("get" + propertyName, new Class[] { Integer.TYPE });
        Method indexedWriteMethod = beanClass.getMethod("set" + propertyName, new Class[] { Integer.TYPE, String.class });
        IndexedPropertyDescriptor ipd = new IndexedPropertyDescriptor(propertyName, readMethod, writeMethod, indexedReadMethod, indexedWriteMethod);
        IndexedPropertyDescriptor ipd2 = new IndexedPropertyDescriptor(propertyName, beanClass);
        assertFalse(ipd.equals(ipd2));
    }
}
