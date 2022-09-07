class BackupThread extends Thread {
    private void startFetchingChannels() {
        Channel o1 = new Channel();
        try {
            m_channels = new ArrayList<Channel>();
            dbHelper = new DatabaseHelper(context);
            Cursor cur = dbHelper.getChannelsBlock_Feed(recID);
            while (cur.moveToNext()) {
                o1 = new Channel();
                o1.setId((cur.getInt((cur.getColumnIndex("_id")))));
                o1.setName(cur.getString(cur.getColumnIndex("ChannelName")));
                o1.setRssLink((cur.getString(cur.getColumnIndex("RssLink"))));
                o1.setFlag(cur.getInt(cur.getColumnIndex("flag")));
                m_channels.add(o1);
                recID = String.valueOf(o1.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
