class BackupThread extends Thread {
    private void createDataSet() {
        ContentValues v = new ContentValues();
        String myLink = getChannelLink();
        String categories = getCategories();
        v.put(News.Channel.CHANNEL_NAME, mChannelName.getText().toString());
        v.put(News.Channel.CHANNEL_LINK, myLink);
        v.put(News.Channel.CHANNEL_CATEGORIES, categories);
        v.put(News.Channel.NOTIFY_NEW, (mNotificationBox.isChecked() ? 1 : 0));
        v.put(News.Channel.UPDATE_MSGS, (mUpdateBox.isChecked() ? 1 : 0));
        v.put(News.Channel.UPDATE_CYCLE, getUpdateCycle());
        if (feedType.equals(News.FEED_TYPE_RSS)) {
            v.put(News.Channel.CHANNEL_TYPE, News.CHANNEL_TYPE_RSS);
        } else if (feedType.equals(News.FEED_TYPE_ATOM)) {
            v.put(News.Channel.CHANNEL_TYPE, News.CHANNEL_TYPE_ATM);
        } else {
            Log.e(_TAG, "Unrecognized Feed Type. How did tis happen?");
        }
        if (v.containsKey(News.Channel.CHANNEL_TYPE)) {
            cUri = mNews.insert(News.Channel.CONTENT_URI, v);
            feedID = Long.parseLong(cUri.getLastPathSegment());
            mCursor = managedQuery(cUri, CHANNEL_PROJECTION, null, null, null);
        } else {
        }
        insertIfNotExistsCategories(categories);
        Log.d(_TAG, "returned uri >>" + cUri + "<<");
    }
}
