class BackupThread extends Thread {
    public void testIndexedPropertyDescriptorStringMethodMethodMethodMethod() throws SecurityException, NoSuchMethodException, IntrospectionException {
        String propertyName = "PropertyFour";
        Class<MockJavaBean> beanClass = MockJavaBean.class;
        Method readMethod = beanClass.getMethod("get" + propertyName, (Class[]) null);
        Method writeMethod = beanClass.getMethod("set" + propertyName, new Class[] { String[].class });
        Method indexedReadMethod = beanClass.getMethod("get" + propertyName, new Class[] { Integer.TYPE });
        Method indexedWriteMethod = beanClass.getMethod("set" + propertyName, new Class[] { Integer.TYPE, String.class });
        IndexedPropertyDescriptor ipd = new IndexedPropertyDescriptor(propertyName, readMethod, writeMethod, indexedReadMethod, indexedWriteMethod);
        assertEquals(readMethod, ipd.getReadMethod());
        assertEquals(writeMethod, ipd.getWriteMethod());
        assertEquals(indexedReadMethod, ipd.getIndexedReadMethod());
        assertEquals(indexedWriteMethod, ipd.getIndexedWriteMethod());
        assertEquals(String[].class, ipd.getPropertyType());
        assertEquals(String.class, ipd.getIndexedPropertyType());
        assertFalse(ipd.isBound());
        assertFalse(ipd.isConstrained());
        assertNull(ipd.getPropertyEditorClass());
        assertEquals(propertyName, ipd.getDisplayName());
        assertEquals(propertyName, ipd.getName());
        assertEquals(propertyName, ipd.getShortDescription());
        assertNotNull(ipd.attributeNames());
        assertFalse(ipd.isExpert());
        assertFalse(ipd.isHidden());
        assertFalse(ipd.isPreferred());
    }
}
