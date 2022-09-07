class BackupThread extends Thread {
    public long position(final Clob pattern, final long start) throws SQLException {
        long patternLength;
        if (start < 1) {
            throw Util.outOfRangeArgument("start: " + start);
        } else if ((patternLength = pattern == null ? 0 : pattern.length()) == 0) {
            return -1L;
        } else if (patternLength > Integer.MAX_VALUE) {
            throw Util.outOfRangeArgument("pattern.length(): " + patternLength);
        }
        char[] charPattern;
        if (pattern instanceof JDBCClob) {
            charPattern = ((JDBCClob) pattern).data().toCharArray();
        } else {
            Reader reader = null;
            CharArrayWriter writer = new CharArrayWriter();
            try {
                reader = pattern.getCharacterStream();
                InOutUtil.copy(reader, writer, patternLength);
            } catch (IOException ex) {
                throw Util.sqlException(ex);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException ex) {
                    }
                }
            }
            charPattern = writer.toCharArray();
        }
        return position(charPattern, start);
    }
}
