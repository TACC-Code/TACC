class BackupThread extends Thread {
    @Test(expected = IntrospectionException.class)
    public void propertyDescriptorConstructorShouldPreventReadMethodWithVoidReturnType() throws Exception {
        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(PROPERTY_NAME, readMethodWithVoidReturnType, writeMethod);
        new PropertyDescriptorPropertyInformation(propertyDescriptor);
    }
}
