class BackupThread extends Thread {
    public Cursor getChannelsBlock(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur;
        if (id.equals("0")) {
            cur = db.rawQuery("SELECT * FROM " + channelsTable + " LIMIT 20", null);
        } else {
            cur = db.rawQuery("SELECT * FROM " + channelsTable + " WHERE " + colID + " > " + id + " LIMIT 20", null);
        }
        return cur;
    }
}
