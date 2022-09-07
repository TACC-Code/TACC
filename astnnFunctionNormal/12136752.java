class BackupThread extends Thread {
    private void doWriteReadTest(ContainerDefinitionField result) {
        byte[][] headerBuffer = new byte[1][1024];
        byte[][] dataBuffer = new byte[1][1024];
        int[] headerBufferPosition = new int[1];
        int[] dataBufferPosition = new int[1];
        headerBufferPosition[0] = 0;
        dataBufferPosition[0] = 0;
        candidate.writeState(new BasicOperation(new DefaultPermissionProfile()), WireIdentity.get("a"), headerBuffer, headerBufferPosition, dataBuffer, dataBufferPosition, false);
        result.readState(new BasicOperation(new DefaultPermissionProfile()), dataBuffer[0], 0, dataBufferPosition[0]);
        assertNotSame("Failed to write/read container definition", candidate, result);
        assertEquals("Failed to write/read container definition", candidate, result);
    }
}
