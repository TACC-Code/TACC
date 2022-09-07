class BackupThread extends Thread {
    public boolean setUserPermission(String annohash, String uid, boolean readPerm, boolean writePerm, boolean delegatePerm) {
        int entity_id = 0;
        String query = "";
        try {
            entity_id = getUserID(uid);
            if (entity_id == -1) entity_id = addUser(uid);
            query = "INSERT INTO ACL (ANNOHASH, ACL_ENTITY_ID, READPERM, WRITEPERM, DELEGPERM, SEARCHPERM, PRIORITY) VALUES ('" + annohash + "'," + entity_id + "," + readPerm + "," + writePerm + "," + delegatePerm + ",true,0)";
            if (this.getDatabase().createStatement().executeUpdate(query) < 1) {
                System.out.println("Error executing SQL statement:" + query);
                return false;
            }
        } catch (SQLException e) {
            System.out.println(query);
            e.printStackTrace();
        }
        return true;
    }
}
