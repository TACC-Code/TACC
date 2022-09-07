class BackupThread extends Thread {
    public static void main(String[] args) {
        try {
            DiagSignalHandler.install("SIGINT");
            Class<?> wrappedClass = Class.forName(args[0]);
            String wrappedArgs[] = new String[args.length - 1];
            for (int i = 0; i < wrappedArgs.length; i++) {
                wrappedArgs[i] = args[i + 1];
            }
            Class<?>[] argTypes = new Class[1];
            argTypes[0] = wrappedArgs.getClass();
            Method mainMethod = wrappedClass.getMethod("main", argTypes);
            Object[] argValues = new Object[1];
            argValues[0] = wrappedArgs;
            mainMethod.invoke(wrappedClass, argValues);
        } catch (Exception e) {
            System.out.println("AppWrap exception " + e);
        }
    }
}
