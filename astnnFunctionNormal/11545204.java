class BackupThread extends Thread {
    @Test(expected = IntrospectionException.class)
    public void propertyDescriptorConstructorShouldPreventWriteMethodWithZeroParameters() throws Exception {
        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(PROPERTY_NAME, readMethod, writeMethodWithZeroParameters);
        new PropertyDescriptorPropertyInformation(propertyDescriptor);
    }
}
