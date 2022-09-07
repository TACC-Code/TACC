class BackupThread extends Thread {
    public void testIndexedPropertyDescriptorStringMethodMethodMethodMethod_IndexedRWNull() throws SecurityException, NoSuchMethodException, IntrospectionException {
        String propertyName = "PropertyFour";
        Class<MockJavaBean> beanClass = MockJavaBean.class;
        Method readMethod = beanClass.getMethod("get" + propertyName, (Class[]) null);
        Method writeMethod = beanClass.getMethod("set" + propertyName, new Class[] { String[].class });
        try {
            new IndexedPropertyDescriptor(propertyName, readMethod, writeMethod, null, null);
            fail("Should throw IntrospectionException.");
        } catch (IntrospectionException e) {
        }
    }
}
