class BackupThread extends Thread {
    @Test
    public void mockingInstanceMethodOfFinalSystemClassWorks() throws Exception {
        URL url = createMock(URL.class);
        URLConnection urlConnection = createMock(URLConnection.class);
        expect(url.openConnection()).andStubReturn(urlConnection);
        replayAll();
        final SystemClassUser systemClassUser = new SystemClassUser();
        assertSame(urlConnection, systemClassUser.useURL(url));
        verifyAll();
    }
}
