class BackupThread extends Thread {
    @Test
    public final void writeTheClassWhenTheFileAlreadyExist() throws IOException, CoreException, InterruptedException, DOMException {
        final DataTestFileSource dataTestFileSource = new TestFileSourceBuilder().with(context.mock(IVectorInclude.class)).with(context.mock(IFileBuilder.class)).with(context.mock(IFixture.class)).with(context.mock(ITestClassStructure.class)).build();
        final IProgressMonitor monitor = context.mock(IProgressMonitor.class);
        TestFileSource toTest = new TestFileSource(dataTestFileSource);
        context.checking(new Expectations() {

            {
                oneOf(dataTestFileSource.fileClassBuilder).isExist();
                will(returnValue(true));
                oneOf(dataTestFileSource.fileClassBuilder).write(dataTestFileSource.mockClassInclude, monitor);
                oneOf(dataTestFileSource.fileClassBuilder).write(dataTestFileSource.classOfTest, monitor);
                oneOf(dataTestFileSource.fileClassBuilder).write(dataTestFileSource.emptyFixture, monitor);
                exactly(3).of(monitor).worked(1);
            }
        });
        toTest.write(monitor);
    }
}
