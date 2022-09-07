class BackupThread extends Thread {
    public void testIndexedPropertyDescriptorStringMethodMethodMethodMethod_IndexedWriteMethodNull() throws SecurityException, NoSuchMethodException, IntrospectionException {
        String propertyName = "PropertyFour";
        Class<MockJavaBean> beanClass = MockJavaBean.class;
        Method readMethod = beanClass.getMethod("get" + propertyName, (Class[]) null);
        Method writeMethod = beanClass.getMethod("set" + propertyName, new Class[] { String[].class });
        Method indexedReadMethod = beanClass.getMethod("get" + propertyName, new Class[] { Integer.TYPE });
        IndexedPropertyDescriptor ipd = new IndexedPropertyDescriptor(propertyName, readMethod, writeMethod, indexedReadMethod, null);
        assertNull(ipd.getIndexedWriteMethod());
        assertEquals(String[].class, ipd.getPropertyType());
        assertEquals(String.class, ipd.getIndexedPropertyType());
    }
}
