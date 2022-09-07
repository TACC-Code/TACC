class BackupThread extends Thread {
    public EscapeCodeEatingConsole(Console console) {
        this(console.reader(), console.writer());
    }
}
