class BackupThread extends Thread {
    private void checkFile(String remoteFileName, long remoteFileSize) {
        if (task.getFileSize() > 0 && task.getFileSize() != remoteFileSize) {
            task.writeMessage("Task", Messages.TaskThread2_MSG_FileSize_Change_Restart);
            logger.info("文件大小不一至和，重新下载！");
            task.reset();
            task.setStatus(Task.STATUS_RUNNING);
        }
        task.setFileSize(remoteFileSize);
        if (task.getFinishedSize() == 0 && !StringUtils.isEmpty(remoteFileName) && !task.getFileName().equals(remoteFileName)) {
            task.setFileName(remoteFileName);
        }
    }
}
