class BackupThread extends Thread {
    protected boolean enoughValidLH(int k) {
        int i = p;
        int n = tokens.size();
        int no = 0;
        while (i < n) {
            if (((Token) tokens.get(i)).getChannel() == channel) if (++no == k) return true;
            i++;
        }
        return false;
    }
}
