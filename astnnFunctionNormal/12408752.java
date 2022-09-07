class BackupThread extends Thread {
    public static FileChannel openChannel(@NonNull File file) {
        return openInputStream(file).getChannel();
    }
}
