class BackupThread extends Thread {
    @NegativeExponential(cycleType = CycleType.CYCLETIME, cycleMean = 4000, cycleDeviation = 2)
    @BenchmarkOperation(name = "PUT", max90th = 2, timing = Timing.MANUAL)
    public void doPut() throws IOException {
        String fileName = uploadPrefix + putSequence++;
        logger.finest("Putting " + fileName);
        FileInputStream upload = new FileInputStream(localFileName);
        int count;
        ctx.recordTime();
        OutputStream ftpOut = ftpClient.put(fileName);
        while ((count = upload.read(buffer)) != -1) ftpOut.write(buffer, 0, count);
        ftpOut.close();
        ctx.recordTime();
        upload.close();
    }
}
