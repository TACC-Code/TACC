class BackupThread extends Thread {
    @BenchmarkOperation(name = "GET", max90th = 2, timing = Timing.MANUAL)
    public void doGet() throws IOException {
        int fileNo = random.random(1, fileCount);
        String fileName = "File" + fileNo;
        logger.finest("Getting " + fileName);
        FileOutputStream download = new FileOutputStream(localFileName);
        int count;
        ctx.recordTime();
        InputStream ftpIn = ftpClient.get(fileName);
        while ((count = ftpIn.read(buffer)) != -1) download.write(buffer, 0, count);
        ftpIn.close();
        ctx.recordTime();
        download.close();
    }
}
