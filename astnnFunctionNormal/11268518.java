class BackupThread extends Thread {
    @Test
    public void testDownloadInterceptor() throws Exception {
        File log4j = ResourceUtils.getFile("classpath:log4j.properties");
        FileChannel fc = new FileInputStream(log4j).getChannel();
        ByteBuffer bb = ByteBuffer.allocate(1024 * 10);
        fc.read(bb);
        bb.flip();
        byte[] realContent = new byte[bb.limit()];
        bb.get(realContent);
        Method method = ReflectionUtils.findMethod(POJOTestAction.class, "download", InfrastructureKeys.METHOD_PARAM);
        stub(mockRequest.getAttribute(InfrastructureKeys.EXECUTION)).toReturn(method);
        final byte[] actualContent = new byte[realContent.length];
        stub(mockResponse.getOutputStream()).toReturn(new ServletOutputStream() {

            @Override
            public void write(int b) throws IOException {
                actualContent[utils.getCounter().get()] = (byte) b;
                utils.increaseCounter();
            }
        });
        String direction = download.preHandle(mockRequest, mockResponse, action);
        verify(mockResponse).setContentType("application/properties");
        assertEquals(Forward.INPUT, direction);
        assertTrue("应该得到log4j.properties文件的字节流", Arrays.equals(realContent, actualContent));
    }
}
