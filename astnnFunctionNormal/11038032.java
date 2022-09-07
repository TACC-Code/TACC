class BackupThread extends Thread {
    public int process() {
        OperationProcessor proc = new OperationProcessor(inputfilereader, outputfilewriter, errorfilewriter, rejectedlineswriter, ops, inputnbcol, outputnbcol, true);
        proc.process();
        try {
            AggregationProcessor proc2 = new AggregationProcessor(aggsource, aggtarget, aggkey, aggamount, aggheader, verbose);
            proc2.process();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
