class BackupThread extends Thread {
    private void checkFeed() {
        mProgressDialog = ProgressDialog.show(ChannelSettings.this, getString(R.string.update), getString(R.string.looking_up_feed_information));
        new Thread() {

            @Override
            public void run() {
                ChannelParser parser = new ChannelParser(ChannelSettings.this);
                String rpc = getChannelLink();
                Log.v(_TAG, "url " + rpc);
                InputStream is = parser.fetch(rpc);
                Integer channelType = parser.parse(is);
                if (channelType != null) {
                    if (channelType == News.CHANNEL_TYPE_ATM) {
                        updateAtomChannel(rpc, false);
                    } else if (channelType == News.CHANNEL_TYPE_RSS) {
                        updateRSSChannel(rpc, false);
                    } else {
                        runOnUiThread(new Runnable() {

                            public void run() {
                                Toast.makeText(ChannelSettings.this, getString(R.string.unknown_feed_type), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } else {
                    runOnUiThread(new Runnable() {

                        public void run() {
                            Toast.makeText(ChannelSettings.this, getString(R.string.invalid_feed_url), Toast.LENGTH_LONG).show();
                        }
                    });
                }
                runOnUiThread(new Runnable() {

                    public void run() {
                        mProgressDialog.dismiss();
                    }
                });
            }
        }.start();
    }
}
