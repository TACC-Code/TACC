class BackupThread extends Thread {
    public String getSubString(final long pos, final int length) throws SQLException {
        Reader reader = null;
        CharArrayWriter writer = null;
        try {
            final int initialCapacity = Math.min(InOutUtil.DEFAULT_COPY_BUFFER_SIZE, length);
            reader = getCharacterStream(pos, length);
            writer = new CharArrayWriter(initialCapacity);
            InOutUtil.copy(reader, writer, length);
        } catch (SQLException ex) {
            throw ex;
        } catch (Exception ex) {
            throw Util.sqlException(ex);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception ex) {
                }
            }
        }
        return writer.toString();
    }
}
