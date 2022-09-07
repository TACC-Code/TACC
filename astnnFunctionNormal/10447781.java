class BackupThread extends Thread {
    private RC readPage(FileDescriptor fd, PageNum pageNum, byte[] dest) {
        String pfMessage = "Reading (" + fd + ", " + pageNum.getPageNum() + "). \n";
        writeLog(pfMessage);
        pStatisticsMgr.register(Statistic.PF_READPAGE, Stat_Operation.STAT_ADDONE, null);
        long offset = pageNum.getPageNum() * (long) pageSize + PF_Internal.PF_FILE_HDR_SIZE;
        FileInputStream fis = new FileInputStream(fd);
        FileChannel fc = fis.getChannel();
        ByteBuffer bb = ByteBuffer.wrap(dest);
        long num = 0;
        try {
            if ((fc = fc.position(offset)) == null) return RC.PF_UNIX;
            num = fc.read(bb);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (num < 0) return RC.PF_UNIX; else if (num != pageSize) return RC.PF_INCOMPLETEREAD; else return RC.PF_SUCCESS;
    }
}
