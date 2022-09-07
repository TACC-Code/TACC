class BackupThread extends Thread {
    public boolean setGroupPermission(String annohash, String gid, boolean readPerm, boolean writePerm, boolean delegatePerm, boolean searchPerm) {
        int entity_id = 0;
        String query = "";
        try {
            entity_id = getGroupID(gid);
            if (entity_id == -1) return false;
            query = "INSERT INTO ACL (ANNOHASH, ACL_ENTITY_ID, READPERM, WRITEPERM, DELEGPERM, SEARCHPERM, PRIORITY) VALUES ('" + annohash + "'," + entity_id + "," + readPerm + "," + writePerm + "," + delegatePerm + "," + searchPerm + ",0)";
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
