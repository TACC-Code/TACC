class BackupThread extends Thread {
    @Test
    public void testParseInsertMeasurementResponse() throws IOException, SosClientException, SosExceptionReport {
        final URL url = new URL("jres://SOSResponses/52North/v1/InsertObservationResponse.xml");
        final InputStream response = url.openStream();
        assertEquals("wrong observation id.", "o_1", con.receiveInsertMeasurementResponse(response));
    }
}
