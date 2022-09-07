class BackupThread extends Thread {
    public ResultTable decode(Message<?> message, ByteBuffer buffer) throws IOException, ClassNotFoundException, IndexOutOfBoundsException {
        buffer.rewind();
        if (this.gzip) {
            ByteArrayInputStream ba_in = new ByteArrayInputStream(buffer.array());
            GZIPInputStream gz_in = new GZIPInputStream(ba_in);
            ByteArrayOutputStream ba_out = new ByteArrayOutputStream(buffer.capacity());
            while (gz_in.available() == 1) {
                ba_out.write(gz_in.read());
            }
            buffer = ByteBuffer.wrap(ba_out.toByteArray());
        }
        DecodingStream in = new DecodingStream(buffer.array());
        short numColumns = in.decodeShort();
        if (numColumns < 0) {
            throw new IndexOutOfBoundsException("Buffer indicates a negative number of columns.");
        }
        ColumnsDefinition columns = new ColumnsDefinition(numColumns);
        for (int i = 0; i < numColumns; i++) {
            columns.add(decodeColumn(in));
        }
        columns.setFinalized();
        ResultTable result = new ResultTable(columns);
        int numRows = in.decodeInteger();
        for (int i = 0; i < numRows; i++) {
            result.add(decodeRow(in, columns));
        }
        return result;
    }
}
