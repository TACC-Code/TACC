class BackupThread extends Thread {
    @Before
    public void before() throws Exception {
        readMethod = Bean.class.getMethod("getFirstName");
        writeMethod = Bean.class.getMethod("setFirstName", String.class);
        readMethodWithVoidReturnType = Bean.class.getMethod("getNothing");
        writeMethodWithZeroParameters = Bean.class.getMethod("setNoNames");
        writeMethodWithTwoParameters = Bean.class.getMethod("setFirstNameAndLastName", String.class, String.class);
        propertyDescriptor = new PropertyDescriptor(PROPERTY_NAME, readMethod, writeMethod);
    }
}
