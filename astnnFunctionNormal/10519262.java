class BackupThread extends Thread {
    @Test
    public void testLock() throws CoreException, IOException {
        IFolder folder = project.getFolder(new Path("src"));
        IFile file = folder.getFile("test.simplemodel");
        IFile classFile = folder.getFile(new Path("Test1.java"));
        file.create(new ByteArrayInputStream("package Test{options{lock none;} class Test1{property SomeProperty : java.lang.String;}}".getBytes()), true, null);
        ResourcesPlugin.getWorkspace().build(IncrementalProjectBuilder.FULL_BUILD, null);
        assertTrue(classFile.exists());
        assertEquals(TestUtil.getAstString(new StringBuilder("public class Test1 ").append("extends TestBase{").append("public Test1(){}").append("private String someProperty;").append("public void setSomeProperty(String someProperty)").append("{this.someProperty=someProperty;}").append("public String getSomeProperty(){").append("return someProperty;}}").toString()), TestUtil.getAstString(classFile));
        classFile.delete(true, null);
        file.setContents(new ByteArrayInputStream("package Test{options{lock synchronized;} class Test1{property SomeProperty : java.lang.String;}}".getBytes()), true, true, null);
        ResourcesPlugin.getWorkspace().build(IncrementalProjectBuilder.FULL_BUILD, null);
        assertTrue(classFile.exists());
        assertEquals(TestUtil.getAstString(new StringBuilder("public class Test1 ").append("extends TestBase{").append("public Test1(){}").append("private String someProperty;").append("public synchronized void setSomeProperty(String someProperty)").append("{this.someProperty=someProperty;}").append("public synchronized String getSomeProperty(){").append("return someProperty;}}").toString()), TestUtil.getAstString(classFile));
        classFile.delete(true, null);
        file.setContents(new ByteArrayInputStream("package Test{options{lock reentrant;} class Test1{property SomeProperty : java.lang.String;}}".getBytes()), true, true, null);
        ResourcesPlugin.getWorkspace().build(IncrementalProjectBuilder.FULL_BUILD, null);
        assertTrue(classFile.exists());
        assertEquals(TestUtil.getAstString(new StringBuilder("public class Test1 ").append("extends TestBase{").append("public Test1(){}").append("private String someProperty;").append("public void setSomeProperty(String someProperty)").append("{LOCK.lock();try{").append("this.someProperty=someProperty;").append("}finally{LOCK.unlock();}}").append("public String getSomeProperty(){").append("LOCK.lock();try{").append("return someProperty;}finally{").append("LOCK.unlock();}}}").toString()), TestUtil.getAstString(classFile));
        classFile.delete(true, null);
        file.setContents(new ByteArrayInputStream("package Test{options{lock readwrite;} class Test1{property SomeProperty : java.lang.String;}}".getBytes()), true, true, null);
        ResourcesPlugin.getWorkspace().build(IncrementalProjectBuilder.FULL_BUILD, null);
        assertTrue(classFile.exists());
        assertEquals(TestUtil.getAstString(new StringBuilder("public class Test1 ").append("extends TestBase{").append("public Test1(){}").append("private String someProperty;").append("public void setSomeProperty(String someProperty)").append("{LOCK.writeLock().lock();try{").append("this.someProperty=someProperty;").append("}finally{LOCK.writeLock().unlock();}}").append("public String getSomeProperty(){").append("LOCK.readLock().lock();try{").append("return someProperty;}finally{").append("LOCK.readLock().unlock();}}}").toString()), TestUtil.getAstString(classFile));
        classFile.delete(true, null);
    }
}
