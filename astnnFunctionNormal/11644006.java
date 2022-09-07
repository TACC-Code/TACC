class BackupThread extends Thread {
    public void testSetIndexedWriteMethod_badargtype() throws IntrospectionException, NoSuchMethodException, NoSuchMethodException {
        String propertyName = "PropertyFour";
        Class<MockJavaBean> beanClass = MockJavaBean.class;
        Method readMethod = beanClass.getMethod("get" + propertyName, (Class[]) null);
        Method writeMethod = beanClass.getMethod("set" + propertyName, new Class[] { String[].class });
        Method indexedReadMethod = beanClass.getMethod("get" + propertyName, new Class[] { Integer.TYPE });
        IndexedPropertyDescriptor ipd = new IndexedPropertyDescriptor(propertyName, readMethod, writeMethod, indexedReadMethod, null);
        assertNull(ipd.getIndexedWriteMethod());
        Method badArgType = beanClass.getMethod("set" + propertyName, new Class[] { Integer.TYPE, Integer.TYPE });
        try {
            ipd.setIndexedWriteMethod(badArgType);
            fail("Should throw IntrospectionException");
        } catch (IntrospectionException e) {
        }
    }
}
