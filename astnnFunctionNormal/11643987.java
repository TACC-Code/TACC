class BackupThread extends Thread {
    public void testIndexedPropertyDescriptorStringMethodMethodMethodMethod_propEmpty() throws SecurityException, NoSuchMethodException {
        String propertyName = "PropertyFour";
        Class<MockJavaBean> beanClass = MockJavaBean.class;
        Method readMethod = beanClass.getMethod("get" + propertyName, (Class[]) null);
        Method writeMethod = beanClass.getMethod("set" + propertyName, new Class[] { String[].class });
        Method indexedReadMethod = beanClass.getMethod("get" + propertyName, new Class[] { Integer.TYPE });
        Method indexedWriteMethod = beanClass.getMethod("set" + propertyName, new Class[] { Integer.TYPE, String.class });
        try {
            new IndexedPropertyDescriptor("", readMethod, writeMethod, indexedReadMethod, indexedWriteMethod);
            fail("Should throw IntrospectionException.");
        } catch (IntrospectionException e) {
        }
    }
}
