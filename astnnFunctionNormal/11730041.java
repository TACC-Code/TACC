class BackupThread extends Thread {
    public void setBlob(int i, Blob x) throws SQLException {
        if (x instanceof jdbcBlob) {
            setParameter(i, ((jdbcBlob) x).data);
            return;
        } else if (x == null) {
            setParameter(i, null);
            return;
        }
        checkSetParameterIndex(i, false);
        final long length = x.length();
        if (length > Integer.MAX_VALUE) {
            String msg = "Maximum Blob input octet length exceeded: " + length;
            throw Util.sqlException(Trace.INPUTSTREAM_ERROR, msg);
        }
        HsqlByteArrayOutputStream out = null;
        try {
            out = new HsqlByteArrayOutputStream();
            java.io.InputStream in = x.getBinaryStream();
            int buffSize = 2048;
            byte[] buff = new byte[buffSize];
            for (int left = (int) length; left > 0; ) {
                int read = in.read(buff, 0, left > buffSize ? buffSize : left);
                if (read == -1) {
                    break;
                }
                out.write(buff, 0, read);
                left -= read;
            }
            setParameter(i, out.toByteArray());
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
