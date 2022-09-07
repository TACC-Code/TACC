class BackupThread extends Thread {
    @Test
    public void testParseExceptionReport() throws IOException, SosClientException {
        final URL url = new URL("jres://SOSResponses/52North/v1/ExceptionDocGetObs.xml");
        final InputStream response = url.openStream();
        try {
            con.receiveInsertMeasurementResponse(response);
            fail("exception report not thrown.");
        } catch (SosExceptionReport r) {
            assertTrue("wrong exception code.", r.getMessage().contains("InvalidParameterValue"));
        }
    }
}
