class BackupThread extends Thread {
        @Override
        public void run() {
            String status = "";
            try {
                URL url = new URL(mUrl.toString());
                URLConnection connection = url.openConnection();
                connection.setConnectTimeout(10000);
                connection.connect();
                InputStream in = connection.getInputStream();
                parseRSS(in, mAdapter);
                status = "done";
            } catch (Exception e) {
                status = "failed:" + e.getMessage();
            }
            final String temp = status;
            if (isCurrentWorker(this)) {
                mHandler.post(new Runnable() {

                    public void run() {
                        mStatusText.setText(temp);
                    }
                });
            }
        }
}
