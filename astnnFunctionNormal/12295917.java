class BackupThread extends Thread {
    public int upgrade() {
        int count = 0;
        Connection fromConn = DbUpgrader.getV10Connection();
        Connection toConn = DbUpgrader.getV20Connection();
        PreparedStatement fromPstmt = null;
        PreparedStatement toUserPstmt = null;
        PreparedStatement toUserPropsPstmt = null;
        PreparedStatement toGroupUserPstmt = null;
        ResultSet rs = null;
        try {
            String oldForumUrl = "";
            fromPstmt = fromConn.prepareStatement(SQL_FROM_FORUM_URL);
            rs = fromPstmt.executeQuery();
            if (rs.next()) {
                oldForumUrl = rs.getString(1);
            }
            rs.close();
            fromPstmt.close();
            PropertyManager pm = PropMan.getInstance(Cnjbb.PROPERTY_FILEANME);
            String cnjbbHome = pm.getProperty("cnjbb2.home");
            String avatarHome = cnjbbHome + File.separator + Cnjbb.AVATAR_PATH;
            String uploadHome = cnjbbHome + File.separator + Cnjbb.UPLOAD_PATH;
            fromPstmt = fromConn.prepareStatement(FROM_COUNT_SQL);
            rs = fromPstmt.executeQuery();
            int totalCount = 0;
            if (rs.next()) {
                totalCount = rs.getInt(1);
            }
            rs.close();
            fromPstmt.close();
            fromPstmt = fromConn.prepareStatement(SQL_FROM_LOAD);
            toUserPstmt = toConn.prepareStatement(SQL_TO_STORE_USER);
            toUserPropsPstmt = toConn.prepareStatement(SQL_TO_STORE_USERPROPS);
            toGroupUserPstmt = toConn.prepareStatement(SQL_TO_STORE_GROUPUSER);
            for (int i = 0; i < totalCount; i += LIMIT) {
                fromPstmt.clearParameters();
                fromPstmt.setInt(1, i);
                fromPstmt.setInt(2, LIMIT);
                rs = fromPstmt.executeQuery();
                while (rs.next()) {
                    int userID = rs.getInt(1);
                    String username = rs.getString(2);
                    String email = rs.getString(3);
                    boolean showEmail = rs.getInt(4) == 1;
                    String password = rs.getString(5);
                    String sign = rs.getString(6);
                    String sex = rs.getString(7);
                    String homepage = rs.getString(8);
                    String registerDate = rs.getString(9);
                    int loginCount = rs.getInt(10);
                    String avatar = rs.getString(11);
                    int avatarWidth = rs.getInt(12);
                    int avatarHeight = rs.getInt(13);
                    String qq = rs.getString(14);
                    String lastLoginDate = rs.getString(15);
                    boolean treeView = rs.getInt(16) == 1;
                    boolean locked = rs.getInt(17) == 1;
                    int level = rs.getInt(18);
                    int wp = rs.getInt(19);
                    int ep = rs.getInt(20);
                    int cp = rs.getInt(21);
                    String title = rs.getString(22);
                    String realName = rs.getString(23);
                    String icq = rs.getString(24);
                    String msn = rs.getString(25);
                    String telephone = rs.getString(26);
                    String company = rs.getString(27);
                    String companySite = rs.getString(28);
                    String brief = rs.getString(29);
                    String birthday = rs.getString(30);
                    registerDate = convertDate(registerDate);
                    lastLoginDate = convertDate(lastLoginDate);
                    birthday = convertDate(birthday);
                    int status = 0;
                    status |= treeView ? 0x0008 : 0;
                    status |= showEmail ? 0x0020 : 0;
                    status |= locked ? 0x0040 : 0;
                    int[] groups = new int[level >= 18 ? 2 : 1];
                    groups[0] = 4;
                    if (level >= 18) {
                        groups[1] = 21 - level;
                        if (groups[1] > 4) {
                            groups[1] = 4;
                        }
                    }
                    if (avatar != null && !avatar.equals("")) {
                        if (avatar.startsWith("pic/Image")) {
                            avatar = Function.stringToArray(avatar, "pic/Image")[1];
                            avatar = Function.stringToArray(avatar, "\\.")[0];
                            if (avatar.length() == 1) {
                                avatar = "0" + avatar;
                            }
                            avatar = "images/avatars/" + avatar + ".gif";
                        } else if (avatar.startsWith("pic/user") && !oldForumUrl.equals("")) {
                            try {
                                String path = avatarHome + File.separator + userID;
                                URLConnection urlConnection = new URL(oldForumUrl + "/" + avatar).openConnection();
                                InputStream is = urlConnection.getInputStream();
                                OutputStream os = new FileOutputStream(new File(path));
                                byte[] buffer = new byte[256];
                                int len = -1;
                                while ((len = is.read(buffer, 0, buffer.length)) != -1) {
                                    os.write(buffer, 0, len);
                                }
                                os.flush();
                                os.close();
                                is.close();
                                avatar = "attachment/avatar/" + userID;
                            } catch (Exception e) {
                                avatar = "";
                            }
                        } else if (avatar.startsWith("attachment.jsp?attid=")) {
                            String path = avatar.substring(21, avatar.length());
                            path = uploadHome + File.separator + userID + File.separator + path;
                            File file = new File(path);
                            if (file.exists()) {
                                try {
                                    path = avatarHome + File.separator + userID;
                                    InputStream is = new FileInputStream(file);
                                    OutputStream os = new FileOutputStream(path);
                                    byte[] buffer = new byte[256];
                                    int len = -1;
                                    while ((len = is.read(buffer, 0, buffer.length)) != -1) {
                                        os.write(buffer, 0, len);
                                    }
                                    os.flush();
                                    os.close();
                                    is.close();
                                    avatar = "attachment/avatar/" + userID;
                                } catch (Exception e1) {
                                    avatar = "";
                                }
                            } else {
                                avatar = "";
                            }
                        }
                    }
                    try {
                        toUserPstmt.clearParameters();
                        toUserPstmt.setInt(1, userID);
                        toUserPstmt.setString(2, username);
                        toUserPstmt.setString(3, password);
                        toUserPstmt.setString(4, registerDate == null ? "" : registerDate);
                        toUserPstmt.setString(5, "");
                        toUserPstmt.setString(6, "");
                        toUserPstmt.setString(7, email == null ? "" : email);
                        toUserPstmt.setString(8, realName == null ? "" : realName);
                        toUserPstmt.executeUpdate();
                        toUserPropsPstmt.clearParameters();
                        toUserPropsPstmt.setInt(1, userID);
                        toUserPropsPstmt.setString(2, sign == null ? "" : sign);
                        toUserPropsPstmt.setString(3, sex);
                        toUserPropsPstmt.setString(4, homepage == null ? "" : homepage);
                        toUserPropsPstmt.setInt(5, 0);
                        toUserPropsPstmt.setString(6, avatar);
                        toUserPropsPstmt.setInt(7, avatarWidth);
                        toUserPropsPstmt.setInt(8, avatarHeight);
                        toUserPropsPstmt.setString(9, qq == null ? "" : qq);
                        toUserPropsPstmt.setString(10, lastLoginDate);
                        toUserPropsPstmt.setString(11, lastLoginDate);
                        toUserPropsPstmt.setInt(12, 0);
                        toUserPropsPstmt.setInt(13, level);
                        toUserPropsPstmt.setInt(14, wp);
                        toUserPropsPstmt.setInt(15, ep);
                        toUserPropsPstmt.setInt(16, cp);
                        toUserPropsPstmt.setString(17, title == null ? "" : title);
                        toUserPropsPstmt.setString(18, icq == null ? "" : icq);
                        toUserPropsPstmt.setString(19, msn == null ? "" : msn);
                        toUserPropsPstmt.setString(20, telephone == null ? "" : telephone);
                        toUserPropsPstmt.setString(21, company == null ? "" : company);
                        toUserPropsPstmt.setString(22, companySite == null ? "" : companySite);
                        toUserPropsPstmt.setString(23, brief == null ? "" : brief);
                        toUserPropsPstmt.setString(24, birthday == null ? "" : birthday);
                        toUserPropsPstmt.setString(25, "");
                        toUserPropsPstmt.setInt(26, status);
                        toUserPropsPstmt.executeUpdate();
                        try {
                            for (int j = 0; j < groups.length; j++) {
                                toGroupUserPstmt.clearParameters();
                                toGroupUserPstmt.setInt(1, groups[j]);
                                toGroupUserPstmt.setInt(2, userID);
                                toGroupUserPstmt.setInt(3, 0);
                                toGroupUserPstmt.executeUpdate();
                            }
                        } catch (Exception e) {
                        }
                        count++;
                    } catch (Exception e) {
                        log.error(e);
                    }
                }
                rs.close();
            }
        } catch (SQLException e) {
            log.error(e);
            e.printStackTrace();
        } finally {
            DbUpgrader.closeDB(rs, fromPstmt, null, fromConn);
            DbUpgrader.closeDB(null, toUserPstmt, null, null);
            DbUpgrader.closeDB(null, toUserPropsPstmt, null, null);
            DbUpgrader.closeDB(null, toGroupUserPstmt, null, toConn);
        }
        return count;
    }
}
