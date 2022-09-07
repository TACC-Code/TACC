class BackupThread extends Thread {
    public void testEquals() throws SecurityException, NoSuchMethodException, IntrospectionException {
        String propertyName = "PropertyFour";
        Class<MockJavaBean> beanClass = MockJavaBean.class;
        Method readMethod = beanClass.getMethod("get" + propertyName, (Class[]) null);
        Method writeMethod = beanClass.getMethod("set" + propertyName, new Class[] { String[].class });
        Method indexedReadMethod = beanClass.getMethod("get" + propertyName, new Class[] { Integer.TYPE });
        Method indexedWriteMethod = beanClass.getMethod("set" + propertyName, new Class[] { Integer.TYPE, String.class });
        IndexedPropertyDescriptor ipd = new IndexedPropertyDescriptor(propertyName, readMethod, writeMethod, indexedReadMethod, indexedWriteMethod);
        IndexedPropertyDescriptor ipd2 = new IndexedPropertyDescriptor(propertyName, beanClass);
        assertTrue(ipd.equals(ipd2));
        assertTrue(ipd.equals(ipd));
        assertTrue(ipd2.equals(ipd));
        assertFalse(ipd.equals(null));
    }
}
