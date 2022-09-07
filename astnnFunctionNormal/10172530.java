class BackupThread extends Thread {
    protected void writeCorrelation() {
        try {
            ProcessTimer corrWriteTimer = new ProcessTimer("Dissimilarity.writeCorrelation()");
            FileChannel disOutChannel = new RandomAccessFile(outfile, "rw").getChannel();
            DataOutputStream stream = new DataOutputStream(Channels.newOutputStream(disOutChannel));
            printHeader(stream, expfile, disType, null);
            MappedByteBuffer disMappedByteBuffer = disOutChannel.map(FileChannel.MapMode.READ_WRITE, disOutChannel.position(), (expfile.numGenes() * (expfile.numGenes() - 1) * 2));
            for (int row = 1; row < expfile.numGenes() && !cancel; row++) {
                for (int column = 0; column < row; column++) {
                    disMappedByteBuffer.putFloat(expfile.correlation(row, column));
                    progress.addValue(1);
                }
            }
            stream.close();
            corrWriteTimer.finish();
            if (!cancel) completed = true;
        } catch (Exception e2) {
        }
    }
}
