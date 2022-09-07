class BackupThread extends Thread {
    public void UpdateContent(InputStreamReader reader) throws NpsException {
        if (reader == null) return;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "update article set content=empty_clob() where id=?";
            pstmt = ctxt.GetConnection().prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.executeUpdate();
            if (pstmt != null) try {
                pstmt.close();
            } catch (Exception e1) {
            }
            sql = "select content from article where id=? for update";
            pstmt = ctxt.GetConnection().prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                oracle.sql.CLOB clob = (oracle.sql.CLOB) rs.getClob(1);
                java.io.Writer writer = clob.getCharacterOutputStream();
                int read = 0;
                char[] buf = new char[1024];
                while ((read = reader.read(buf)) >= 0) {
                    writer.write(buf, 0, read);
                }
                writer.flush();
                try {
                    writer.close();
                } catch (Exception e1) {
                }
                try {
                    reader.close();
                } catch (Exception e1) {
                }
            }
            Clear("content");
        } catch (Exception e) {
            com.microfly.util.DefaultLog.error(e);
        } finally {
            try {
                rs.close();
            } catch (Exception e1) {
            }
            try {
                pstmt.close();
            } catch (Exception e1) {
            }
        }
    }
}
