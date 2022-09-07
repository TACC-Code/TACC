class BackupThread extends Thread {
    public boolean compile() {
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
        if (result.getErrors().length > 0 || result.getWarnings().length > 0) return false;
        return true;
    }
}
