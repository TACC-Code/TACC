class BackupThread extends Thread {
    private void weaveClassFile(File file, File origin, File dest) throws IOException {
        InputStream is = new FileInputStream(file);
        DataInputStream dis = new DataInputStream(is);
        ClassFile cf = new ClassFile(dis);
        System.out.println("ClassName '" + cf.getName() + "'");
        ClassPool cp = ClassPool.getDefault();
        byte[] modifiedBytecode = null;
        try {
            CtClass loadedClass = cp.get(cf.getName());
            System.out.println("Loaded");
            modifiedBytecode = this.getClassFileTransformer(new AllClassesFilter(true)).transform(loadedClass.toBytecode(), cf.getName());
            System.out.println("Bytecode modified");
            if (modifiedBytecode == null) {
                modifiedBytecode = loadedClass.toBytecode();
            }
            System.out.println("Bytecode ready to write");
            System.out.println("Detached");
            loadedClass.defrost();
            CtClass modifiedClass = cp.makeClass(new ByteArrayInputStream(modifiedBytecode));
            System.out.println("Modified class done");
            modifiedClass.writeFile(dest.getAbsolutePath());
            System.out.println("Wrote");
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
    }
}
