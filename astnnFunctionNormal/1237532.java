class BackupThread extends Thread {
    public String runClass() {
        String back = "Runtime Error";
        if (classEntity == null || classEntity.getClassName().isEmpty() || classEntity.getPackageName().isEmpty() || classEntity.getContent().isEmpty()) throw new RuntimeException();
        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        PrintWriter out = new PrintWriter(writer);
        out.write(classEntity.getContent());
        out.close();
        final MemoryResourceReader reader = new MemoryResourceReader();
        reader.add(classEntity.getPackageName().replace(".", "/") + "/" + classEntity.getClassName() + ".java", writer.toByteArray());
        final ResourceStore store = new MemoryResourceStore();
        final String[] resource = cmd.getArgs();
        final CompilationResult result = compiler.compile(resource, reader, store, classloader);
        this.classEntity.setBytecode(store.read(classEntity.getPackageName().replace(".", "/") + "/" + classEntity.getClassName() + ".class"));
        if (result.getErrors().length > 0 || result.getWarnings().length > 0) return "Compiler Errors";
        MemoClassLoader classLoader = new MemoClassLoader();
        Class<?> clazz = classLoader.defineClass(store, classEntity.getPackageName().replace(".", "/") + "/" + classEntity.getClassName() + ".class");
        Method mainMethod;
        try {
            mainMethod = clazz.getMethod("main", String[].class);
            ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
            PrintStream printOut = new PrintStream(byteOutput);
            System.setOut(printOut);
            mainMethod.invoke(null, new Object[] { null });
            if (byteOutput.size() > 0) return byteOutput.toString();
        } catch (Exception e) {
            back = e.toString();
            e.printStackTrace();
            return back;
        }
        return back;
    }
}
