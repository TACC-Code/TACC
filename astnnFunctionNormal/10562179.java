class BackupThread extends Thread {
    public void forums() {
        try {
            write("# -- vbullmin - Forums");
            Statement sta = DB.conn.createStatement();
            ResultSet result = sta.executeQuery("SELECT id, title FROM forums ORDER BY id ASC");
            int count = 0;
            while (result.next()) {
                write("INSERT INTO phpbb_forums (forum_id, " + "forum_name, forum_desc, cat_id, forum_order, " + "forum_posts, forum_topics, forum_last_post_id, " + "auth_view, auth_read, auth_post, auth_reply, " + "auth_edit, auth_delete, auth_announce, auth_sticky, " + "auth_pollcreate, auth_vote, auth_attachments) " + "VALUES (" + result.getInt(1) + ", '" + DB.escape(result.getString(2)) + "', NULL, 1, " + count + ", " + 0 + ", " + 0 + ", 1, 0, 0, 1, 1, 1, 1, 3, 3, 1, 1, 3);");
                count++;
                GUI.progress.setValue(count);
            }
        } catch (SQLException e) {
            DB.exception(e);
        }
    }
}
