class BackupThread extends Thread {
    public void writeResource(File file, Resource rsrc, ProgressReporter prg, String fillVal) throws VisbardException {
        fFile = file;
        if (fFile == null) throw new VisbardException("Missing output file");
        fResource = rsrc;
        if (fResource == null) throw new VisbardException("Missing resource");
        fConfig = rsrc.getCurrentConfig();
        ResourceReader reader = fResource.getReader();
        if (prg != null) {
            prg.setTaskTitle("Writing Data");
            prg.setProgress(0);
        }
        initVariables(fConfig);
        reader.open();
        try {
            fWriter.write(reader.getCentricity());
            fWriter.newLine();
            fWriter.write(fHeaderLine);
            fWriter.newLine();
        } catch (IOException e) {
            throw new VisbardException(e.getMessage());
        }
        Range timeRange = fConfig.getTimeRange();
        long numreadings = reader.getNumReadings(timeRange);
        sLogger.info("Found " + numreadings + " readings in range " + VisbardDate.getString(timeRange) + ".");
        int d = fConfig.getDecimation();
        int toread = (int) (Math.floor(numreadings / d));
        sLogger.info("Decimation factor " + d + ". Saving " + toread + " readings.");
        if (toread == 0) throw new VisbardException("Nothing to output");
        reader.reset();
        reader.fastForward(timeRange.fStart);
        String line = "";
        int rcount = 0;
        double[] data;
        int linesToSkip = (int) d - 1;
        if (prg != null) prg.setProgress(0.2f);
        for (int j = 0; j < toread; j++) {
            if (prg != null) prg.setProgress((((float) j / toread) * 0.8f) + 0.2f);
            reader.skip(linesToSkip);
            Reading reading = reader.next();
            line = "";
            if (reading != null) {
                rcount++;
                for (int i = 0; i < fCols.size(); i++) {
                    Column col = (Column) fCols.get(i);
                    data = reading.get(col);
                    if (col.getName().equals(CategoryType.TIME.getName())) {
                        if (Double.isNaN(data[0])) {
                            String misdata = "---";
                            line += misdata;
                            line += tab(line);
                            line += misdata;
                            line += tab(line);
                            line += misdata;
                            line += tab(line);
                        } else {
                            double timeData = data[0];
                            if (col.getUnit().toString().equals("sec1970")) {
                                timeData *= 1000d;
                                timeData += 62168472000000d - 1252800000l;
                            }
                            long[] date = Epoch.breakdown(timeData);
                            Calendar cal = new GregorianCalendar((int) date[0], (int) date[1] - 1, (int) date[2], (int) date[3], (int) date[4], (int) date[5]);
                            cal.add(Calendar.MILLISECOND, (int) date[6]);
                            double year = (double) date[0];
                            double day = cal.get(Calendar.DAY_OF_YEAR);
                            double hour = (double) date[3] + ((double) date[4] / 60d) + ((double) date[5] / (60 * 60d) + ((double) date[6] / (1000 * 60 * 60d)));
                            line += Double.toString(year);
                            line += tab(line);
                            line += Double.toString(day);
                            line += tab(line);
                            line += Double.toString(hour);
                            line += tab(line);
                        }
                    } else if (col.isScalar()) {
                        if (Double.isNaN(data[0])) {
                            line += fillVal;
                        } else {
                            line += Double.toString(data[0]);
                        }
                        line += tab(line);
                    } else {
                        if (Double.isNaN(data[0]) || Double.isNaN(data[1]) || (Double.isNaN(data[2]))) {
                            String misdata = fillVal;
                            line += misdata;
                            line += tab(line);
                            line += misdata;
                            line += tab(line);
                            line += misdata;
                            line += tab(line);
                        } else {
                            line += Double.toString(data[0]);
                            line += tab(line);
                            line += Double.toString(data[1]);
                            line += tab(line);
                            line += Double.toString(data[2]);
                            line += tab(line);
                        }
                    }
                }
                try {
                    fWriter.write(line);
                    fWriter.newLine();
                } catch (IOException e) {
                    throw new VisbardException(e.getMessage());
                }
            }
        }
        try {
            fWriter.close();
        } catch (IOException e) {
            throw new VisbardException(e.getMessage());
        }
        reader.close();
    }
}
