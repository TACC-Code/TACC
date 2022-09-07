class BackupThread extends Thread {
    @Test
    public void testGetByQuery_bin() throws Exception {
        ResponseImpl response = (ResponseImpl) service.getByQuery("imatinib", "tab25-bin", "0", "200", "n");
        PsicquicStreamingOutput pso = (PsicquicStreamingOutput) response.getEntity();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        pso.write(baos);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        GZIPInputStream gzipInputStream = new GZIPInputStream(bais);
        ByteArrayOutputStream mitabOut = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len;
        while ((len = gzipInputStream.read(buf)) > 0) mitabOut.write(buf, 0, len);
        gzipInputStream.close();
        mitabOut.close();
        Assert.assertEquals(11, mitabOut.toString().split("\n").length);
    }
}
