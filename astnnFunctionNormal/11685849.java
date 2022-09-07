class BackupThread extends Thread {
    public synchronized String generateBranchId() {
        long num = rand.nextLong() + Utils.counter++ + System.currentTimeMillis();
        byte bid[] = digester.digest(Long.toString(num).getBytes());
        return SIPConstants.BRANCH_MAGIC_COOKIE + Utils.toHexString(bid);
    }
}
