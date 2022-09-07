class BackupThread extends Thread {
    @Test
    public void mockingURLWorks() throws Exception {
        URL url = createMock(URL.class);
        URLConnection urlConnectionMock = createMock(URLConnection.class);
        expect(url.openConnection()).andReturn(urlConnectionMock);
        replayAll();
        assertSame(url.openConnection(), urlConnectionMock);
        verifyAll();
    }
}
