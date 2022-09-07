class BackupThread extends Thread {
    private void showUserDetail(final User user) {
        setContentView(R.layout.profile);
        try {
            this.setTitle(user.getName());
            final TextView name = (TextView) findViewById(R.id.userName);
            name.setText(user.getUrl());
            name.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(user.getUrl()));
                    startActivity(browserIntent);
                }
            });
            final ImageView imageView = (ImageView) findViewById(R.id.userImageView);
            String imageUrl = user.getPhotoUrl();
            if ((imageUrl != null) && !imageUrl.equals("")) {
                URL url = new URL(Configuration.getDomain() + imageUrl);
                URLConnection conn = url.openConnection();
                conn.connect();
                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                Bitmap bm = BitmapFactory.decodeStream(bis);
                bis.close();
                imageView.setImageBitmap(bm);
            }
        } catch (Exception e) {
            Log.i(tag, e.getMessage());
        }
    }
}
