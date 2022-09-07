class BackupThread extends Thread {
    private void saveDataSet() {
        String myLink = getChannelLink();
        ContentValues cv = new ContentValues();
        cv.put(Channel.CHANNEL_NAME, mChannelName.getText().toString());
        cv.put(Channel.CHANNEL_LINK, myLink);
        cv.put(Channel.UPDATE_CYCLE, getUpdateCycle());
        String categories = getCategories();
        cv.put(Channel.CHANNEL_CATEGORIES, categories);
        cv.put(Channel.NOTIFY_NEW, (mNotificationBox.isChecked() ? 1 : 0));
        cv.put(Channel.UPDATE_MSGS, (mUpdateBox.isChecked() ? 1 : 0));
        getContentResolver().update(Uri.withAppendedPath(Channel.CONTENT_URI, String.valueOf(feedID)), cv, null, null);
        insertIfNotExistsCategories(categories);
        Intent intent = new Intent(this, NewsreaderService.class);
        intent.putExtra(NewsreaderService.EXTRA_UPDATE_FEED, String.valueOf(feedID));
        startService(intent);
    }
}
