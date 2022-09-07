class BackupThread extends Thread {
                public void onError(Request request, Throwable exception) {
                    Log.write("failed file reading" + exception);
                }
}
