class BackupThread extends Thread {
    public void testSetIndexedWriteMethod_return() throws IntrospectionException, NoSuchMethodException, NoSuchMethodException {
        String propertyName = "PropertyFour";
        Class<MockJavaBean> beanClass = MockJavaBean.class;
        Method readMethod = beanClass.getMethod("get" + propertyName, (Class[]) null);
        Method writeMethod = beanClass.getMethod("set" + propertyName, new Class[] { String[].class });
        Method indexedReadMethod = beanClass.getMethod("get" + propertyName, new Class[] { Integer.TYPE });
        IndexedPropertyDescriptor ipd = new IndexedPropertyDescriptor(propertyName, readMethod, writeMethod, indexedReadMethod, null);
        assertNull(ipd.getIndexedWriteMethod());
        Method badArgType = beanClass.getMethod("setPropertyFourInvalid", new Class[] { Integer.TYPE, String.class });
        ipd.setIndexedWriteMethod(badArgType);
        assertEquals(String.class, ipd.getIndexedPropertyType());
        assertEquals(String[].class, ipd.getPropertyType());
        assertEquals(Integer.TYPE, ipd.getIndexedWriteMethod().getReturnType());
    }
}
