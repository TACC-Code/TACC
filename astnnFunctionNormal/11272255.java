class BackupThread extends Thread {
    private void buildDynamicMBeanInfo() throws IntrospectionException {
        Constructor[] constructors = this.getClass().getConstructors();
        dConstructors[0] = new MBeanConstructorInfo("AppenderDynamicMBean(): Constructs a AppenderDynamicMBean instance", constructors[0]);
        BeanInfo bi = Introspector.getBeanInfo(appender.getClass());
        PropertyDescriptor[] pd = bi.getPropertyDescriptors();
        int size = pd.length;
        for (int i = 0; i < size; i++) {
            String name = pd[i].getName();
            Method readMethod = pd[i].getReadMethod();
            Method writeMethod = pd[i].getWriteMethod();
            if (readMethod != null) {
                Class returnClass = readMethod.getReturnType();
                if (isSupportedType(returnClass)) {
                    String returnClassName;
                    if (returnClass.isAssignableFrom(Priority.class)) {
                        returnClassName = "java.lang.String";
                    } else {
                        returnClassName = returnClass.getName();
                    }
                    dAttributes.add(new MBeanAttributeInfo(name, returnClassName, "Dynamic", true, writeMethod != null, false));
                    dynamicProps.put(name, new MethodUnion(readMethod, writeMethod));
                }
            }
        }
        MBeanParameterInfo[] params = new MBeanParameterInfo[0];
        dOperations[0] = new MBeanOperationInfo("activateOptions", "activateOptions(): add an appender", params, "void", MBeanOperationInfo.ACTION);
        params = new MBeanParameterInfo[1];
        params[0] = new MBeanParameterInfo("layout class", "java.lang.String", "layout class");
        dOperations[1] = new MBeanOperationInfo("setLayout", "setLayout(): add a layout", params, "void", MBeanOperationInfo.ACTION);
    }
}
