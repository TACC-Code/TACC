class BackupThread extends Thread {
    private void saveMealsToDisk(Context context, InputStream is) {
        try {
            FileOutputStream fos = context.openFileOutput("meals.xml", Context.MODE_PRIVATE);
            byte[] b = new byte[1024];
            int read;
            while ((read = is.read(b)) != -1) fos.write(b, 0, read);
        } catch (Exception e) {
        }
    }
}
