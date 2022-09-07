class BackupThread extends Thread {
    public void setBinaryStream(int parameterIndex, java.io.InputStream x, int length) throws SQLException {
        checkSetParameterIndex(parameterIndex, true);
        if (x == null) {
            throw Util.sqlException(Trace.error(Trace.INVALID_JDBC_ARGUMENT, Trace.JDBC_NULL_STREAM));
        }
        HsqlByteArrayOutputStream out = null;
        try {
            out = new HsqlByteArrayOutputStream();
            int size = 2048;
            byte[] buff = new byte[size];
            for (int left = length; left > 0; ) {
                int read = x.read(buff, 0, left > size ? size : left);
                if (read == -1) {
                    break;
                }
                out.write(buff, 0, read);
                left -= read;
            }
            setParameter(parameterIndex, out.toByteArray());
        } catch (IOException e) {
            throw Util.sqlException(Trace.INPUTSTREAM_ERROR, e.toString());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
