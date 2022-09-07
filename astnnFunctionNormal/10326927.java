class BackupThread extends Thread {
    @Override
    public void write(DataOutput out) throws IOException {
        readID.write(out);
        flag.write(out);
        refname.write(out);
        lpos.write(out);
        mapQ.write(out);
        sequence.write(out);
        quality.write(out);
        mismatchnum.write(out);
    }
}
