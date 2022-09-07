class BackupThread extends Thread {
    public boolean insertMemberByJDBC(Members member) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tr = null;
        String sql = "insert into jrun_members(uid,username,password,email,groupid) values(?,?,?,?,?)";
        PreparedStatement pstmt = null;
        Connection conn = null;
        try {
            tr = session.beginTransaction();
            conn = session.connection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, member.getUid());
            pstmt.setString(2, member.getUsername());
            pstmt.setString(3, member.getPassword());
            pstmt.setString(4, member.getEmail());
            pstmt.setShort(5, member.getGroupid());
            pstmt.executeUpdate();
            tr.commit();
            return true;
        } catch (SQLException e) {
            if (tr != null) {
                tr.rollback();
            }
            e.printStackTrace();
        } finally {
            try {
                if (tr != null) {
                    tr = null;
                }
                if (pstmt != null) {
                    pstmt.close();
                    pstmt = null;
                }
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
