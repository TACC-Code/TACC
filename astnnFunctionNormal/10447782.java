class BackupThread extends Thread {
    private RC writePage(FileDescriptor fd, PageNum pageNum, byte[] source) {
        String psMessage = "Reading (" + fd + ", " + pageNum.getPageNum() + "). \n";
        writeLog(psMessage);
        pStatisticsMgr.register(Statistic.PF_READPAGE, Stat_Operation.STAT_ADDONE, null);
        long offset = pageNum.getPageNum() * (long) pageSize + PF_Internal.PF_FILE_HDR_SIZE;
        FileOutputStream fos = new FileOutputStream(fd);
        FileChannel fc = fos.getChannel();
        ByteBuffer bb = ByteBuffer.wrap(source);
        long num = 0;
        try {
            if ((fc = fc.position(offset)) == null) return RC.PF_UNIX;
            num = fc.write(bb);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (num < 0) return RC.PF_UNIX; else if (num != pageSize) return RC.PF_INCOMPLETEWRITE; else return RC.PF_SUCCESS;
    }
}
