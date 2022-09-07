class BackupThread extends Thread {
    @Override
    public void doSplit(ProgressPublisher pp, Recording oldRec, Recording newRec, CutMark splitPoint) {
        if (oldRec == null || newRec == null || oldRec.getPath().compareTo(newRec.getPath()) == 0) {
            getLogger().info("new recording has same path as old one, so no split at all!");
            return;
        }
        if (oldRec.getType().compareTo(Recording.VARIANT_PES_REC) != 0) {
            getLogger().error("Sorry, no split operation on non-Pes recordings!");
            return;
        }
        if (pp != null && !pp.isActive()) pp.start();
        File oldRecDir = oldRec.getPath();
        File newRecDir = newRec.getPath();
        List<Long> sizes = determineFileSizes(oldRec);
        int progress = 0;
        getLogger().info("performSplit at: " + splitPoint.getByteOffset() + " (FO:" + splitPoint.getFileOffset() + "), starting at file #" + splitPoint.getFileNumber());
        if (pp != null) pp.configure(0, 10);
        if (pp != null) pp.publishProgress(progress++, "create directory");
        try {
            newRecDir.mkdirs();
            if (newRecDir.exists() && newRecDir.isDirectory()) {
                int newIndex = 1;
                if (pp != null) pp.publishProgress(progress++, "copy file with splitpoint");
                File destFile = newRec.getVideoFile(newIndex);
                File srcFile = oldRec.getVideoFile(splitPoint.getFileNumber());
                FileUtils.copyFile(destFile, srcFile);
                if (pp != null) pp.publishProgress(progress++, "move rest of movie files");
                for (int i = splitPoint.getFileNumber() + 1; i < sizes.size(); i++) {
                    destFile = newRec.getVideoFile(++newIndex);
                    srcFile = oldRec.getVideoFile(i);
                    FileUtils.moveOrRename(destFile, srcFile);
                }
                if (pp != null) pp.publishProgress(progress++, "reindex old movie");
                doReIndex(oldRec);
                if (pp != null) pp.publishProgress(progress++, "reindex new movie");
                doReIndex(newRec);
                File indexFile = newRec.getKeyFile();
                if (indexFile.exists() && indexFile.length() > 20) {
                    if (pp != null) pp.publishProgress(progress++, "calculate new cutmarks for both recordings");
                    List<CutMark>[] res = splitCutmarks(newRec, oldRec.getCutMarks(), splitPoint);
                    oldRec.setCutMarks(res[0]);
                    newRec.setCutMarks(res[1]);
                    if (pp != null) pp.publishProgress(progress++, "save new recordings to persistence");
                    TransactionFactory taf = (TransactionFactory) ApplicationServiceProvider.getService(TransactionFactory.class);
                    Transaction ta = taf.createTransaction();
                    ta.add(new TOSave<Recording>(oldRec));
                    ta.add(new TOSave<Recording>(newRec));
                    ta.execute();
                    if (pp != null) pp.publishProgress(progress++, "create info.vdr for new movie");
                    File infoSrc = new File(oldRecDir, oldRec.isPesRecording() ? "info.vdr" : "info");
                    File infoDest = new File(newRecDir, infoSrc.getName());
                    FileUtils.copyFile(infoDest, infoSrc);
                    updateInfo(oldRec);
                    updateInfo(newRec);
                    File cmx = new File(oldRec.getPath(), "marks.pjx");
                    if (cmx.exists()) writePjxCutmarks(oldRec, cmx); else writeVdrCutmarks(oldRec);
                    writeVdrCutmarks(newRec);
                }
                if (pp != null) pp.publishProgress(progress++, "done");
            }
        } catch (InvalidSplitOperationException iso) {
            getLogger().error("What the ...", iso);
            throw iso;
        } catch (Exception e) {
            getLogger().error("Oups", e);
        } finally {
            if (pp != null && pp.isActive()) pp.end();
        }
    }
}
