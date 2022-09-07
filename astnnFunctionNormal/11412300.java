class BackupThread extends Thread {
    public static void main(final String[] args) {
        long time = System.currentTimeMillis();
        long elapsedTime;
        final Deliberator2 app = new Deliberator2(args);
        try {
            AbstractMSWriter writer = null;
            String outFileExtension = JPLFile.getExtension(app.paramManager.getOutputFileName()).toLowerCase();
            if (outFileExtension.equals(SPTXTReader.EXTENSION)) {
                writer = app.new SPTXTDecoyWriter();
            } else if (outFileExtension.equals(MSPReader.EXTENSION)) {
                writer = app.new MSPDecoyWriter();
            } else {
                System.err.println("no writer for file " + app.paramManager.getOutputFileName());
                System.exit(1);
            }
            File inFile = new File(app.paramManager.getFilename());
            File outFile = new File(app.paramManager.getOutputFileName());
            if (app.paramManager.isConcatLibs()) {
                FileUtils.copyFile(inFile, outFile);
            }
            writer.enableAppendingMode(true);
            writer.open(outFile);
            if (app.paramManager.getLogFileName().length() > 0) {
                app.logWriter = new BufferedWriter(new FileWriter(app.paramManager.getLogFileName()));
            }
            if (app.paramManager.isRenderingEnabled()) {
                if (app.paramManager.isVerbose()) {
                    System.out.println("preparing filesystem for rendering...");
                }
                app.prepareFilesystem();
                elapsedTime = System.currentTimeMillis() - time;
                time = System.currentTimeMillis();
                if (app.paramManager.isVerbose()) {
                    System.out.println("done [" + elapsedTime + " ms]");
                }
            }
            app.mslibIter = app.parseMSLibFile();
            if (app.paramManager.getSamplingProbability() == -1) {
                if (app.paramManager.getDistFile() != null) {
                    MS1MS2_PEAKS_READER.parse(app.paramManager.getDistFile());
                    if (app.paramManager.isVerbose()) {
                        System.out.println(" computing max ratios from " + app.paramManager.getDistFile() + "...");
                    }
                    app.dists = MS1MS2_PEAKS_READER.toMS1MS2PeakRatioDists(app.paramManager.getDistFile());
                } else {
                    if (app.paramManager.isVerbose()) {
                        System.out.println("compute MS1/MS2 distributions...");
                    }
                    app.dists = new MS1MS2PeakDists.Builder(app.mslibIter, PL_TRANSFORMER).ms2BinWidth(1).ms1BinWidth(50).smoothingLevel(2).build();
                    elapsedTime = System.currentTimeMillis() - time;
                    time = System.currentTimeMillis();
                    if (app.paramManager.isVerbose()) {
                        System.out.println("done [" + elapsedTime + " ms]");
                    }
                }
                if (app.paramManager.isVerbose()) {
                    System.out.println("compute MS1/MS2 max ratios...");
                }
                app.computeMS1MS2Ratios();
                elapsedTime = System.currentTimeMillis() - time;
                time = System.currentTimeMillis();
                if (app.paramManager.isVerbose()) {
                    System.out.println("done [" + elapsedTime + " ms]");
                }
            }
            if (app.paramManager.isVerbose()) {
                System.out.println("sampling na peaks from mslib...");
            }
            app.mslibIter = app.parseMSLibFile();
            Map<Integer, HistogramDataSet> naPeaksSample = app.samplingNAPeaks(app.mslibIter);
            elapsedTime = System.currentTimeMillis() - time;
            time = System.currentTimeMillis();
            if (app.paramManager.isVerbose()) {
                System.out.println("done [" + elapsedTime + " ms]");
            }
            MS_MATCHER.setTolerance(app.paramManager.getTol());
            int countScan = 0;
            app.pb.setIndeterminate(false);
            app.pb.setMinimum(0);
            app.pb.setMaximum(count);
            if (app.paramManager.isVerbose()) {
                System.out.println("creating decoy mslib (" + count + " scans)...");
            }
            ((DecoyWriter) writer).enableDecoyTag(true);
            time = System.currentTimeMillis();
            app.mslibIter = app.parseMSLibFile();
            while (app.mslibIter.hasNext()) {
                MSScan scan = app.mslibIter.next();
                double score = 1.0;
                PeakList scanPl = scan.getPeakList();
                MSScan scanDecoy = null;
                PeakList decoyPl = null;
                PeakList decoyPlMin = null;
                double minScore = Double.MAX_VALUE;
                int countShuffling = 1;
                while ((countShuffling < MAX_SHUFFLING_TRIES) && (score >= app.paramManager.getDpt())) {
                    if (app.logWriter != null) {
                        app.logWriter.append("> scan " + scan.getScanNum() + ":\n");
                        final Peptide pept = scanPl.getPrecursor().getPeptide();
                        app.logWriter.append(pept.toAAString());
                    }
                    scanDecoy = app.createDecoySpectrum(scan, naPeaksSample);
                    decoyPl = scanDecoy.getPeakList();
                    if (app.logWriter != null) {
                        app.logWriter.append(" -> " + scanDecoy.getPeakList().getPrecursor().getPeptide().toAAString() + "\n");
                    }
                    final PeakList scanAnnotPl = FILTERS_MANAGER.transform(scanPl);
                    final PeakList decoyAnnotPl = FILTERS_MANAGER.transform(decoyPl);
                    MS_MATCHER.computeMatch(scanAnnotPl, decoyAnnotPl);
                    score = MS_MATCHER.getScore();
                    if (score < minScore) {
                        minScore = score;
                        decoyPlMin = decoyPl;
                    }
                    if (app.logWriter != null) {
                        app.logWriter.append("score = " + score + "\n");
                    }
                    countShuffling++;
                }
                decoyPl = decoyPlMin;
                if (app.logWriter != null) {
                    app.logWriter.append("min score = " + minScore + "\n");
                }
                if (app.paramManager.isRenderingEnabled()) {
                    MS_RENDERER.exportChart(scanPl, app.paramManager.getRenderDir() + "/scan/Delib-scan" + scan.getScanNum() + "_" + scanPl.getPrecursor().getPeptide().toAAString(), "scan-" + scan.getScanNum());
                    MS_RENDERER.exportChart(decoyPl, app.paramManager.getRenderDir() + "/scan/Delib-scan" + scan.getScanNum() + "_" + decoyPl.getPrecursor().getPeptide().toAAString() + "_shuffled", "decoy scan-" + scan.getScanNum());
                }
                writer.add(scanDecoy);
                writer.flush();
                countScan++;
                app.pb.setValue(countScan);
            }
            elapsedTime = System.currentTimeMillis() - time;
            time = System.currentTimeMillis();
            if (app.paramManager.isVerbose()) {
                System.out.println("decoy mslib done [" + elapsedTime + " ms]");
            }
            writer.close();
            if (app.logWriter != null) {
                app.logWriter.close();
            }
        } catch (final Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
