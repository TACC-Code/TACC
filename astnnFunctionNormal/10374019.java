class BackupThread extends Thread {
    protected void cleanup(Context context) throws IOException, InterruptedException {
        try {
            int numread = smap.load_reads(max_mismatches, readperround, reads);
            if (numread > 0) {
                for (int j = 0; j < smap.the_seeds.size(); ++j) {
                    context.progress();
                    smap.iterate_over_seeds(refgenome, j, smap.fast_reads, smap.max_mismatches);
                }
                smap.eliminate_ambigs(smap.max_mismatches, smap.fast_reads);
                Vector<SAMRecordWritable> results = new Vector<SAMRecordWritable>();
                smap.generateSAMResults(CloudAligner.INPUT_FORMAT.FASTA_FILE, results);
                for (int i = 0; i < results.size(); i++) context.write(new Text(smap.read_names.elementAt(smap.read_index.elementAt(i))), results.elementAt(i));
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
