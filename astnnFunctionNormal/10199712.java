class BackupThread extends Thread {
    @Test
    public void testParseRegisterSensorResponse() throws IOException, SosClientException, SosExceptionReport {
        final URL url = new URL("jres://SOSResponses/52North/v1/RegisterSensorResponse.xml");
        final InputStream response = url.openStream();
        assertEquals("wrong sensor id.", "urn:ogc:object:feature:Sensor:IFGI:ifgi-sensor-1", con.receiveRegisterSensorResponse(response));
    }
}
