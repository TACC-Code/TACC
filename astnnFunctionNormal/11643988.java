class BackupThread extends Thread {
    public void testIndexedPropertyDescriptorStringMethodMethodMethodMethod_propInvalid() throws SecurityException, NoSuchMethodException, IntrospectionException {
        String propertyName = "PropertyFour";
        String invalidName = "An Invalid Property name";
        Class<MockJavaBean> beanClass = MockJavaBean.class;
        Method readMethod = beanClass.getMethod("get" + propertyName, (Class[]) null);
        Method writeMethod = beanClass.getMethod("set" + propertyName, new Class[] { String[].class });
        Method indexedReadMethod = beanClass.getMethod("get" + propertyName, new Class[] { Integer.TYPE });
        Method indexedWriteMethod = beanClass.getMethod("set" + propertyName, new Class[] { Integer.TYPE, String.class });
        IndexedPropertyDescriptor ipd = new IndexedPropertyDescriptor(invalidName, readMethod, writeMethod, indexedReadMethod, indexedWriteMethod);
        assertEquals(invalidName, ipd.getName());
        assertEquals(String.class, ipd.getIndexedPropertyType());
    }
}
