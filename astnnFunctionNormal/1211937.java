class BackupThread extends Thread {
    public static String getLargeTextField(ResultSet rs, int columnIndex) throws SQLException {
        Reader bodyReader;
        String value;
        try {
            bodyReader = rs.getCharacterStream(columnIndex);
            if (bodyReader == null) return null;
            char buf[] = new char[256];
            StringWriter out = new StringWriter(255);
            int i;
            while ((i = bodyReader.read(buf)) >= 0) out.write(buf, 0, i);
            value = out.toString();
            out.close();
            bodyReader.close();
        } catch (Exception e) {
            return rs.getString(columnIndex);
        }
        if (value == null) return "";
        return value;
    }
}
