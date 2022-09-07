    @Test
    public void testChangeSupportWithLock() throws CoreException, IOException {
        IFolder folder = project.getFolder(new Path("src"));
        IFile file = folder.getFile("test.simplemodel");
        IFile classFile = folder.getFile(new Path("Test1.java"));
        file.create(new ByteArrayInputStream("package Test{options{changesupport both;lock readwrite;} class Test1{property SomeProperty : java.lang.String;}}".getBytes()), true, null);
        ResourcesPlugin.getWorkspace().build(IncrementalProjectBuilder.FULL_BUILD, null);
        assertTrue(classFile.exists());
class BackupThread extends Thread {
        assertEquals(TestUtil.getAstString(new StringBuilder("import java.beans.PropertyVetoException;").append("public class Test1 ").append("extends TestBase{").append("public Test1(){}").append("private String someProperty;").append("public void setSomeProperty(String someProperty){").append("LOCK.writeLock().lock();try{").append("try{fireVetoableChange(\"someProperty\",").append("this.someProperty,someProperty);").append("}catch(PropertyVetoException ex){").append("throw new RuntimeException(ex);}").append("String old_someProperty = this.someProperty;").append("this.someProperty=someProperty;").append("firePropertyChange(\"someProperty\",").append("old_someProperty,someProperty);}").append("finally{LOCK.writeLock().unlock();}}").append("public String getSomeProperty(){").append("LOCK.readLock().lock();try{").append("return someProperty;}").append("finally{LOCK.readLock().unlock();}}").toString()), TestUtil.getAstString(classFile));
    }
}
