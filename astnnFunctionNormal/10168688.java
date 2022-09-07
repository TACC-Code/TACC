class BackupThread extends Thread {
    public int savePreferences(String user_uid, String theWelcomeText, String email, String visible, String nbemail, String nbsms) {
        int flag = 0;
        DBConnection con = null;
        try {
            con = DBServiceManager.allocateConnection();
            con.setAutoCommit(false);
            StringBuffer query = new StringBuffer();
            query.append("DELETE FROM cafe_guestbook_conf ");
            query.append("WHERE cafe_guestbook_conf_fuid=" + user_uid);
            con.executeUpdate(query.toString());
            query = new StringBuffer();
            query.append("INSERT INTO cafe_guestbook_conf ");
            query.append("(cafe_guestbook_conf_fuid,cafe_guestbook_conf_welcome,cafe_guestbook_conf_visible,cafe_guestbook_conf_email,cafe_guestbook_conf_nbsms,cafe_guestbook_conf_nbemail) ");
            query.append("VALUES (" + user_uid + ",'" + theWelcomeText + "'," + visible + ",'" + email + "'," + nbsms + "," + nbemail + ")");
            con.executeUpdate(query.toString());
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException sqle) {
            }
            logError("ERROR: savePreferences(" + user_uid + "," + theWelcomeText + "," + email + "," + visible + "," + nbemail + "," + nbsms + ")", e);
            flag = 1;
        } finally {
            if (con != null) {
                try {
                    con.reset();
                } catch (SQLException e) {
                }
                con.release();
            }
        }
        return flag;
    }
}
