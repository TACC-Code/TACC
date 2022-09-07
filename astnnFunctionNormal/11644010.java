class BackupThread extends Thread {
    public void testSetIndexedReadMethodFollowANullValue() throws Exception {
        try {
            IndexedPropertyDescriptor i = new IndexedPropertyDescriptor("a", DummyBean.class, "readMethod", "writeMethod", null, "indexedReadMethod");
            Method irm = DummyBean.class.getDeclaredMethod("indexedReadMethod", Integer.TYPE);
            i.setIndexedReadMethod(irm);
            fail("should throw IntrospectionException.");
        } catch (IntrospectionException e) {
        }
    }
}
