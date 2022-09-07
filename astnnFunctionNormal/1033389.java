class BackupThread extends Thread {
        FmsChannel getChannel(String jobname) {
            return repo.get(jobname);
        }
}
