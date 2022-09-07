class BackupThread extends Thread {
    private static void createDatabaseFile() {
        AssetManager am = APPLICATION_CONTEXT.getAssets();
        try {
            InputStream in = am.open("VampiDroid.mp3");
            OutputStream out = APPLICATION_CONTEXT.openFileOutput(VAMPIDROID_DB, Context.MODE_PRIVATE);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
