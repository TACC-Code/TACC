class BackupThread extends Thread {
    private static void convert_rev1_to_rev2(IntChatServerSettings icss, Connection conn) throws SQLException, InterruptedException, IOException {
        System.out.println("Database structure conversion from Revision 1 to Revision 2 is started.\n" + "It may take a long time (about an hour or even more!) depending on size of your database and server performance.\n" + "Please, be a patient.");
        try {
            checkStructure(conn);
            System.out.println("Stage 1: database structure is already converted.");
        } catch (SQLException se) {
            URL sqlScriptUrl = null;
            switch(IntChatDatabaseOperations.getDialect()) {
                case IntChatDatabaseOperations.DIALECT_POSTGRESQL:
                    sqlScriptUrl = IntChatDatabaseStructure.class.getResource("/avoware/intchat/server/db/diff_postgresql_rev1_rev2.sql");
                    break;
                case IntChatDatabaseOperations.DIALECT_MYSQL:
                    sqlScriptUrl = IntChatDatabaseStructure.class.getResource("/avoware/intchat/server/db/diff_mysql_rev1_rev2.sql");
                    break;
                default:
                    throw new SQLException("Unsupported SQL dialect");
            }
            System.out.println("Stage 1: convert database structure itself, moving inner data.");
            String[] instructions = simpleParse(sqlScriptUrl.openStream());
            conn.setAutoCommit(false);
            try {
                for (int i = 0; i < instructions.length; i++) {
                    System.out.println("Executing: " + instructions[i] + ";");
                    IntChatDatabaseOperations.executeUpdate(conn, instructions[i]);
                    System.out.println("done...\n");
                }
                System.out.print("Commiting structure changes... ");
                conn.commit();
                System.out.println("done.");
            } finally {
                conn.setAutoCommit(true);
            }
        }
        System.out.println("Stage 1: is completed.");
        System.out.println("Stage 2: we should move file data from file pool into database.");
        String LAST_UPLOADED_FILE_ID = "96b342f5-d367-461c-9ec3-ab0b484d8018";
        long lastUploadedFileId = -1;
        ResultSet rs = IntChatDatabaseOperations.executeQuery(conn, "SELECT paramval FROM ic_systemsettings WHERE paramname='" + LAST_UPLOADED_FILE_ID + "' LIMIT 1");
        if (rs.next()) {
            lastUploadedFileId = Long.parseLong(rs.getString(1));
        } else {
            IntChatDatabaseOperations.executeUpdate(conn, "INSERT INTO ic_systemsettings (paramname, paramval) VALUES ('" + LAST_UPLOADED_FILE_ID + "', '-1')");
        }
        rs = null;
        rs = IntChatDatabaseOperations.executeQuery(conn, "SELECT count(*) FROM ic_messages, ic_recipients WHERE ic_messages.id>" + lastUploadedFileId + " AND ic_messages.tid=(SELECT id FROM ic_messagetypes WHERE templatename='IC_FILES' LIMIT 1) " + "AND ic_messages.id=ic_recipients.mid AND ic_recipients.processed=FALSE");
        if (rs.next()) {
            int filesToUpload = rs.getInt(1);
            System.out.println("Files to upload: " + filesToUpload);
            rs = null;
            String fileSpool = icss.getStringValue("FileSpool");
            if (!(new File(fileSpool).isAbsolute())) fileSpool = IntChatServer.SERVER_DIR_ABSOLUTE_PATH + IntChatServer.FILE_SEPARATOR + fileSpool;
            rs = IntChatDatabaseOperations.executeQuery(conn, "SELECT ic_messages.id, ic_messages.mbody FROM ic_messages, ic_recipients WHERE ic_messages.id>" + lastUploadedFileId + " AND ic_messages.tid=(SELECT id FROM ic_messagetypes WHERE templatename='IC_FILES' LIMIT 1) " + "AND ic_messages.id=ic_recipients.mid AND ic_recipients.processed=FALSE GROUP BY ic_messages.id, ic_messages.mbody ORDER BY ic_messages.id");
            conn.setAutoCommit(false);
            try {
                int currentFile = 0;
                while (rs.next()) {
                    currentFile++;
                    long id = rs.getLong("id");
                    InputStream in = null;
                    try {
                        File f = new File(fileSpool + IntChatServer.FILE_SEPARATOR + rs.getString("mbody"));
                        in = new FileInputStream(f);
                        long fileLength = f.length();
                        System.out.print("Uploading file " + currentFile + " of " + filesToUpload + " (id=" + id + ")... ");
                        if (!avoware.intchat.server.servlet.File.insertBLOB(conn, in, fileLength, id, 0)) {
                            in.close();
                            System.out.print("rolling back transaction... ");
                            conn.rollback();
                            System.out.println("exiting");
                            return;
                        }
                        in.close();
                        IntChatDatabaseOperations.executeUpdate(conn, "UPDATE ic_messages SET mbody='" + fileLength + "' WHERE id=" + id);
                        IntChatDatabaseOperations.executeUpdate(conn, "UPDATE ic_systemsettings SET paramval='" + id + "' WHERE paramname='" + LAST_UPLOADED_FILE_ID + "'");
                    } catch (FileNotFoundException fnfe) {
                        System.out.print("File " + currentFile + " of " + filesToUpload + " (id=" + id + ") is not found, writing FILE_NOT_FOUND info... ");
                        IntChatDatabaseOperations.executeUpdate(conn, "UPDATE ic_recipients SET processed=TRUE, pcomment='" + IntChatConstants.FileOperations.FILE_NOT_FOUND + "', pdate=" + System.currentTimeMillis() + " WHERE mid=" + id + " AND processed=FALSE");
                    }
                    conn.commit();
                    System.out.println("done");
                }
                IntChatDatabaseOperations.executeUpdate(conn, "DELETE FROM ic_systemsettings WHERE paramname='" + DATABASE_REVISION_PARAMNAME + "'");
                IntChatDatabaseOperations.executeUpdate(conn, "INSERT INTO ic_systemsettings (paramname, paramval) VALUES ('" + DATABASE_REVISION_PARAMNAME + "', '2')");
                IntChatDatabaseOperations.executeUpdate(conn, "DELETE FROM ic_systemsettings WHERE paramname='" + LAST_UPLOADED_FILE_ID + "'");
                conn.commit();
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
}
