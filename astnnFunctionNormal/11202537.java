class BackupThread extends Thread {
    public String getValueAsString() throws GoldenGateDatabaseSqlError {
        switch(type) {
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
                return (String) value;
            case Types.BIT:
                return ((Boolean) value).toString();
            case Types.TINYINT:
                return ((Byte) value).toString();
            case Types.SMALLINT:
                return ((Short) value).toString();
            case Types.INTEGER:
                return ((Integer) value).toString();
            case Types.BIGINT:
                return ((Long) value).toString();
            case Types.REAL:
                return ((Float) value).toString();
            case Types.DOUBLE:
                return ((Double) value).toString();
            case Types.VARBINARY:
                return new String((byte[]) value);
            case Types.DATE:
                return ((Date) value).toString();
            case Types.TIMESTAMP:
                return ((Timestamp) value).toString();
            case Types.CLOB:
                {
                    StringBuilder sBuilder = new StringBuilder();
                    Reader reader = ((Reader) value);
                    char[] cbuf = new char[4096];
                    int len;
                    try {
                        len = reader.read(cbuf);
                        while (len > 0) {
                            sBuilder.append(cbuf, 0, len);
                            len = reader.read(cbuf);
                        }
                    } catch (IOException e) {
                        throw new GoldenGateDatabaseSqlError("Error while reading Clob as String", e);
                    }
                    return sBuilder.toString();
                }
            case Types.BLOB:
                {
                    InputStream reader = ((InputStream) value);
                    int len;
                    ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
                    try {
                        len = reader.available();
                        while (len > 0) {
                            buffer.writeBytes(reader, len);
                        }
                    } catch (IOException e) {
                        throw new GoldenGateDatabaseSqlError("Error while reading Clob as String", e);
                    }
                    len = buffer.readableBytes();
                    byte[] dst = new byte[len];
                    buffer.readBytes(dst);
                    return new String((byte[]) dst);
                }
            default:
                throw new GoldenGateDatabaseSqlError("Type unknown: " + type);
        }
    }
}
