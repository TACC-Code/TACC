class BackupThread extends Thread {
    public void petitRoque(int i) {
        board[i + 1] = board[i] % 100;
        board[i] = 0;
        if (!ischeck()) {
            board[i] = board[i + 1];
            board[i + 1] = board[i + 3] % 100;
            board[i + 3] = 0;
            simulize(i, i + 2);
            board[i + 3] = board[i + 1] + 100;
            board[i + 1] = board[i];
        }
        board[i] = board[i + 1] + 100;
        board[i + 1] = 0;
    }
}
