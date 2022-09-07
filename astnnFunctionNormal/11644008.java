class BackupThread extends Thread {
    public void testSetIndexedWriteMethod_InvalidIndexType() throws IntrospectionException, NoSuchMethodException, NoSuchMethodException {
        String propertyName = "PropertyFour";
        Class<MockJavaBean> beanClass = MockJavaBean.class;
        Method readMethod = beanClass.getMethod("get" + propertyName, (Class[]) null);
        Method writeMethod = beanClass.getMethod("set" + propertyName, new Class[] { String[].class });
        Method indexedReadMethod = beanClass.getMethod("get" + propertyName, new Class[] { Integer.TYPE });
        IndexedPropertyDescriptor ipd = new IndexedPropertyDescriptor(propertyName, readMethod, writeMethod, indexedReadMethod, null);
        assertNull(ipd.getIndexedWriteMethod());
        Method badArgType = beanClass.getMethod("setPropertyFourInvalid2", new Class[] { String.class, String.class });
        try {
            ipd.setIndexedWriteMethod(badArgType);
            fail("Should throw IntrospectionException");
        } catch (IntrospectionException e) {
        }
        ipd = new IndexedPropertyDescriptor("data", NormalBean.class);
        ipd.setIndexedReadMethod(null);
        try {
            ipd.setIndexedWriteMethod(NormalBean.class.getMethod("setData", Integer.TYPE, Integer.TYPE));
            fail("should throw IntrospectionException");
        } catch (IntrospectionException e) {
        }
    }
}
