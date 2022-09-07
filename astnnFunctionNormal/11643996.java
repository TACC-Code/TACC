class BackupThread extends Thread {
    public void testIndexedPropertyDescriptorStringMethodMethodMethodMethod_IndexedRWIncompatible() throws SecurityException, NoSuchMethodException, IntrospectionException {
        String propertyName = "PropertyFour";
        String anotherProp = "PropertyFive";
        Class<MockJavaBean> beanClass = MockJavaBean.class;
        Method readMethod = beanClass.getMethod("get" + propertyName, (Class[]) null);
        Method writeMethod = beanClass.getMethod("set" + propertyName, new Class[] { String[].class });
        Method indexedReadMethod = beanClass.getMethod("get" + anotherProp, new Class[] { Integer.TYPE });
        Method indexedWriteMethod = beanClass.getMethod("set" + propertyName, new Class[] { Integer.TYPE, String.class });
        IndexedPropertyDescriptor ipd = new IndexedPropertyDescriptor(propertyName, readMethod, writeMethod, indexedReadMethod, indexedWriteMethod);
        assertEquals(propertyName, ipd.getName());
        assertEquals(String[].class, ipd.getPropertyType());
        assertEquals(String.class, ipd.getIndexedPropertyType());
        indexedReadMethod = beanClass.getMethod("get" + anotherProp, new Class[] { Integer.TYPE, Integer.TYPE });
        try {
            new IndexedPropertyDescriptor(propertyName, readMethod, writeMethod, indexedReadMethod, indexedWriteMethod);
            fail("should throw IntrosecptionException");
        } catch (IntrospectionException e) {
        }
    }
}
