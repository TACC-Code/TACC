class BackupThread extends Thread {
        protected Channel getChannel() {
            return scanVariableParameter.getChannel();
        }
}
