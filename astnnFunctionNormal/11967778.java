class BackupThread extends Thread {
    public Cursor getChannelsBlock_Feed(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + channelsTable + " WHERE FLAG=1 AND " + colID + " > " + id + " LIMIT 20", null);
        return cur;
    }
}
