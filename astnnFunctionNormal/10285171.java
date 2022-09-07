class BackupThread extends Thread {
    @Test
    public void write_and_read_are_balanced() throws EXistException, IOException {
        SymbolTable symbolTable = new SymbolTable(null, tmpDir);
        symbolTable.getSymbol("some-name");
        VariableByteOutputStream mockOs = EasyMock.createMock(VariableByteOutputStream.class);
        VariableByteInput mockIs = EasyMock.createMock(VariableByteInput.class);
        final Capture<Byte> byteCapture = new Capture<Byte>();
        final Capture<Integer> intCapture = new Capture<Integer>();
        final Capture<String> strCapture = new Capture<String>();
        mockOs.writeByte(capture(byteCapture));
        mockOs.writeInt(capture(intCapture));
        mockOs.writeUTF(capture(strCapture));
        replay(mockOs);
        symbolTable.localNameSymbols.write(mockOs);
        verify(mockOs);
        expect(mockIs.available()).andReturn(1);
        expect(mockIs.readByte()).andReturn(byteCapture.getValue());
        expect(mockIs.readInt()).andReturn(intCapture.getValue());
        expect(mockIs.readUTF()).andReturn(strCapture.getValue());
        expect(mockIs.available()).andReturn(0);
        replay(mockIs);
        symbolTable.read(mockIs);
        verify(mockIs);
    }
}
