class BackupThread extends Thread {
    @Test(expected = IntrospectionException.class)
    public void propertyDescriptorConstructorShouldPreventWriteMethodWithMoreThanOneParameter() throws Exception {
        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(PROPERTY_NAME, readMethod, writeMethodWithTwoParameters);
        new PropertyDescriptorPropertyInformation(propertyDescriptor);
    }
}
