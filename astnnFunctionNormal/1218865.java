class BackupThread extends Thread {
    private String getChannelLink() {
        String myLink = mChannelLink.getText().toString();
        Log.d(_TAG, "myLink was>>" + myLink);
        if (myLink != null && !myLink.equals("") && !myLink.startsWith("http://")) {
            myLink = "http://" + myLink;
            Log.d(_TAG, "myLink now>>" + myLink);
        }
        return myLink;
    }
}
