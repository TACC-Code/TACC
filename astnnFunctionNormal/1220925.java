class BackupThread extends Thread {
    public static void main(String[] args) {
        if (!containsSwitch(NO_GUI_COMMAND_LINE_SWITCH, args)) try {
            MainGUI.main(args);
        } catch (Exception e) {
            System.err.println(e);
        }
        if (containsSwitch(MERGE_COMMAND_LINE_SWITCH, args)) {
            String fileName = getStringValueFor(MERGE_SOURCE_DIR_PREFIX, args);
            if (fileName == null) System.err.println(MERGE_SOURCE_DIR_PREFIX + " not specified"); else {
                int targetFileSize = getIntValueFor(MERGE_TARGET_FILE_SIZE_PREFIX, args);
                if (targetFileSize < 1000) targetFileSize = 5000000;
                File freeDBDir = new File(fileName);
                File targetDir = new File(freeDBDir.getAbsolutePath() + MERGE_TARGET_FILE_EXTENSION);
                String targetDirName = getStringValueFor(MERGE_TARGET_DIR_PREFIX, args);
                if (targetDirName != null) targetDir = new File(targetDirName);
                if (freeDBDir.exists() && freeDBDir.canRead() && targetDir.mkdirs() || (targetDir.exists() && targetDir.isDirectory() && targetDir.canWrite())) {
                    File targetSubDir;
                    File[] sourceSubDirs = freeDBDir.listFiles();
                    if ((sourceSubDirs != null) && (sourceSubDirs.length > 0)) {
                        boolean quiet = containsSwitch(QUIET_COMMAND_LINE_SWITCH, args);
                        if (!quiet) Arrays.sort(sourceSubDirs);
                        long startMergeGMT = System.currentTimeMillis();
                        for (int i = 0; i < sourceSubDirs.length; i++) {
                            if (sourceSubDirs[i].exists() && sourceSubDirs[i].isDirectory() && sourceSubDirs[i].canRead()) try {
                                targetSubDir = new File(targetDir.getAbsolutePath() + File.separatorChar + sourceSubDirs[i].getName());
                                if (!quiet) System.out.println("Reading from " + sourceSubDirs[i].getAbsolutePath() + ", writing to " + targetSubDir.getAbsolutePath());
                                if (mergeFreeDBSubDir(sourceSubDirs[i], targetSubDir, targetFileSize, quiet) != 0) System.err.println("Failed to copy " + targetDir.getAbsolutePath() + " completely");
                            } catch (IOException ioException) {
                                System.err.println("Failed to merge " + freeDBDir.getAbsolutePath() + " into " + targetDir.getAbsolutePath());
                                System.err.println(ioException.getMessage());
                            }
                        }
                        RawTextReader reader;
                        RawTextLogger writer;
                        try {
                            reader = new RawTextReader(new File(freeDBDir.getAbsolutePath() + File.separatorChar + "COPYING"));
                            writer = new RawTextLogger(new File(targetDir.getAbsolutePath() + File.separatorChar + "COPYING"));
                            while (reader.hasMoreElements()) writer.println(reader.nextElement());
                            writer.close();
                            reader.close();
                            reader = new RawTextReader(new File(freeDBDir.getAbsolutePath() + File.separatorChar + "README"));
                            writer = new RawTextLogger(new File(targetDir.getAbsolutePath() + File.separatorChar + "README"));
                            while (reader.hasMoreElements()) writer.println(reader.nextElement());
                            writer.close();
                            reader.close();
                            if (!quiet) System.out.println();
                            System.out.println("Merge took " + StringProfile.getTextualDuration(System.currentTimeMillis() - startMergeGMT));
                            System.out.println();
                            System.out.println(mergeFilesBytesRead + "\t\t bytes from\t\t " + mergeFilesRead + "\t files in\t\t " + freeDBDir.getAbsolutePath());
                            System.out.println(mergeFilesBytesWritten + "\t\t bytes to\t\t " + mergeFilesWritten + "\t\t files in\t\t " + targetDir.getAbsolutePath());
                            System.out.println();
                            if (containsSwitch(MERGE_VERIFY_COMAND_LINE_SWITCH, args)) {
                                long sum = 0;
                                FileCDLoader fcl = new FileCDLoader(freeDBDir);
                                long startValidation = System.currentTimeMillis();
                                while (fcl.hasMoreCDs()) {
                                    sum++;
                                    fcl.nextCD();
                                }
                                System.out.println(sum + "\t\t discs parsed from " + freeDBDir.getAbsolutePath() + "\t\t " + StringProfile.getTextualDuration(System.currentTimeMillis() - startValidation));
                                sum = 0;
                                fcl = new FileCDLoader(targetDir);
                                startValidation = System.currentTimeMillis();
                                while (fcl.hasMoreCDs()) {
                                    sum++;
                                    fcl.nextCD();
                                }
                                System.out.println(sum + "\t\t discs parsed from " + targetDir.getAbsolutePath() + "\t " + StringProfile.getTextualDuration(System.currentTimeMillis() - startValidation));
                                System.out.println();
                            }
                        } catch (IOException ioException) {
                            System.err.println("Failed to copy GNU General Public License");
                            System.err.println(ioException.getMessage());
                        }
                    }
                } else System.err.println("Failed to create targetdirectory " + targetDir.getAbsolutePath());
            }
        }
        MySQLConnector.setUpLogging(args);
        MySQLStatementGenerator statementGenerator = getMySQLStatementGeneratorConfiguredFromCommandLine(args);
        CharProfileComparator comparator = null;
        if (containsSwitch(SKIP_LANG_ANNO_COMMAND_LINE_SWITCH, args)) comparator = CharProfileComparator.getEmptyComparator(); else comparator = CharProfileComparator.getDefaultEuroCentricComparator();
        Validator validator = getValidatorFromArgs(args);
        if (containsSwitch(DUMP_DB_SCHEMA_COMMAND_LINE_SWITCH, args)) {
            int numberOfTables = MySQLConnector.FOUR_TABLES;
            if (containsSwitch(CREATE_TWO_TABLES_SWITCH_COMMAND_LINE_SWITCH, args)) numberOfTables = MySQLConnector.TWO_TABLES;
            if (numberOfTables == MySQLConnector.FOUR_TABLES) {
                System.out.print(statementGenerator.getDataBaseInitializeStatementsForFourTabeles());
                System.out.println();
                System.out.print(statementGenerator.getDataBaseIndexPurgeStatementsForFourTabeles());
                System.out.print(statementGenerator.getDataBaseIndexInitializeStatementsForFourTabeles());
            }
            if (numberOfTables == MySQLConnector.TWO_TABLES) {
                System.out.print(statementGenerator.getDataBaseInitializeStatementsForTwoTabeles());
                System.out.println();
                System.out.print(statementGenerator.getDataBaseIndexPurgeStatementsForTwoTabeles());
                System.out.print(statementGenerator.getDataBaseIndexInitializeStatementsForTwoTabeles());
            }
        }
        File sourceToExtractFrom = null;
        if (containsSwitch(EXTRACTION_SOURCE_FILE_PREFIX, args)) sourceToExtractFrom = new File(getStringValueFor(EXTRACTION_SOURCE_FILE_PREFIX, args));
        if (sourceToExtractFrom != null) {
            if (!sourceToExtractFrom.exists()) System.err.println(EXTRACTION_SOURCE_FILE_PREFIX + ' ' + sourceToExtractFrom.getName() + " does not exist"); else if (!sourceToExtractFrom.canRead()) System.err.println(EXTRACTION_SOURCE_FILE_PREFIX + ' ' + sourceToExtractFrom.getName() + " is not readable"); else {
                Logger extractionLogger = new ExceptionReporter();
                try {
                    if (containsSwitch(EXTRACTION_REPORT_COMMAND_LINE_SWITCH, args)) extractionLogger = new ConsoleTextLogger();
                    if (containsSwitch(EXTRACTION_REPORT_FILE_PREFIX, args)) extractionLogger = new RawTextLogger(new File("Extracting from " + sourceToExtractFrom.getName() + ".txt"));
                } catch (IOException iE) {
                    System.err.println(iE.getMessage());
                }
                ExceptionReporter extractionReporter = new ExceptionReporter(extractionLogger);
                ProfilingExtractor profiler = null;
                String fileName = sourceToExtractFrom.getAbsoluteFile() + "." + validator.getClass().getSimpleName() + ProfilingExtractor.PROFILER_EXTENSION;
                try {
                    profiler = ProfilingExtractor.getProfiler(fileName);
                    System.out.println(fileName + " loaded");
                } catch (IOException iE) {
                } catch (ClassNotFoundException cE) {
                }
                if ((profiler == null) || containsSwitch(EVALUATE_FILES_COMMAND_LINE_SWITCH, args)) {
                    try {
                        if (containsSwitch(SPOOL_COMMAND_LINE_SWITCH, args)) profiler = new ProfilingExtractor(sourceToExtractFrom, containsSwitch(EXTRACT_EASY_COMMAND_LINE_SWITCH, args), containsSwitch(EXTRACT_NO_DISC_WITHOUT_RELEASE_YEAR_SET_COMMAND_LINE_SWITCH, args), containsSwitch(EXTRACT_DUPLICATE_DISC_DEFINITIONS, args), new ValidatingAdapter(validator, extractionReporter), extractionReporter); else profiler = new ProfilingExtractor(sourceToExtractFrom, containsSwitch(EXTRACT_EASY_COMMAND_LINE_SWITCH, args), containsSwitch(EXTRACT_NO_DISC_WITHOUT_RELEASE_YEAR_SET_COMMAND_LINE_SWITCH, args), containsSwitch(EXTRACT_DUPLICATE_DISC_DEFINITIONS, args), new ValidatingAnnotatingAdapter(validator, comparator, extractionReporter), extractionReporter);
                    } catch (IOException iE) {
                        System.err.println("Failed to profile " + sourceToExtractFrom.getAbsolutePath());
                        System.err.println(iE.getMessage());
                    }
                    fileName = sourceToExtractFrom.getAbsoluteFile() + "." + validator.getClass().getSimpleName() + ProfilingExtractor.PROFILER_EXTENSION;
                    try {
                        ProfilingExtractor.saveProfiler(profiler, fileName);
                        System.out.println(fileName + " saved");
                    } catch (IOException iE) {
                        System.err.println("Failed to save Profiler to " + fileName);
                        System.err.println(iE.getMessage());
                    }
                    if (!containsSwitch(SPOOL_COMMAND_LINE_SWITCH, args)) {
                        System.out.println();
                        System.out.println(profiler.getOperationSummary());
                        System.out.println(profiler.getExceptionSummary());
                    }
                }
                VarcharFieldLengthAdvisor advisor = null;
                fileName = sourceToExtractFrom.getAbsoluteFile() + "." + validator.getClass().getSimpleName() + OptimalLengthCalculator.OPTIMIZER_EXTENSION;
                try {
                    advisor = OptimalLengthCalculator.getOptimzer(fileName);
                    System.out.println(fileName + " loaded");
                    System.out.println();
                } catch (IOException iE) {
                } catch (ClassNotFoundException cE) {
                }
                if ((advisor == null) && (containsSwitch(SPOOL_COMMAND_LINE_SWITCH, args))) {
                    if (containsSwitch(SKIP_COLUMNS_WIDTHS_CALC_COMMAND_LINE_SWITCH, args) || containsSwitch(SPOOLER_CONTINUE_TABLES_COMMAND_LINE_SWITCH, args)) {
                        advisor = new FixedLengthDummy();
                        System.out.println();
                    } else {
                        advisor = new OptimalLengthCalculator(profiler.getStringProfiles(), 496);
                        advisor.getLengthCommendationForStringsInField("initialise");
                        fileName = sourceToExtractFrom.getAbsoluteFile() + "." + validator.getClass().getSimpleName() + OptimalLengthCalculator.OPTIMIZER_EXTENSION;
                        try {
                            OptimalLengthCalculator.saveOptimizer((OptimalLengthCalculator) advisor, fileName);
                            System.out.println(fileName + " saved");
                        } catch (IOException iE) {
                            System.err.println("Failed to save Optimizer to " + fileName);
                            System.err.println(iE.getMessage());
                        } finally {
                            System.out.println();
                        }
                        System.out.println(advisor.getOperationSummary());
                    }
                }
                System.out.flush();
                if (containsSwitch(SPOOL_COMMAND_LINE_SWITCH, args)) {
                    MySQLConnector.setUpLogging(args);
                    int numberOfTables = MySQLConnector.FOUR_TABLES;
                    if (containsSwitch(CREATE_TWO_TABLES_SWITCH_COMMAND_LINE_SWITCH, args)) numberOfTables = MySQLConnector.TWO_TABLES;
                    MySQLConnector mySQLCDSpooler = null;
                    statementGenerator = new MySQLStatementGenerator(getDBNameFromCommandLine(args));
                    Logger spoolingLogger = new ExceptionReporter();
                    try {
                        if (containsSwitch(SPOOLER_REPORT_COMMAND_LINE_SWITCH, args)) spoolingLogger = new ConsoleTextLogger();
                        if (containsSwitch(SPOOLER_REPORT_FILE_PREFIX, args)) spoolingLogger = new RawTextLogger(new File("Spooling to " + getDBNameFromCommandLine(args) + ".txt"));
                    } catch (IOException iE) {
                        System.err.println(iE.getMessage());
                    }
                    ExceptionReporter spoolingReporter = new ExceptionReporter(spoolingLogger);
                    if (containsSwitch(ANNOTATE_WITHOUT_VALIDATION, args)) mySQLCDSpooler = getMySQLConnectorConfiguredFromCommandLine(args, numberOfTables, new AnnotatingAdapter(), spoolingReporter); else mySQLCDSpooler = getMySQLConnectorConfiguredFromCommandLine(args, numberOfTables, new ValidatingAnnotatingAdapter(validator, comparator, spoolingReporter), spoolingReporter);
                    if (mySQLCDSpooler == null) {
                        if (!containsSwitch(SPOOLER_REPORT_COMMAND_LINE_SWITCH, args)) System.out.println("Failed to connect MySQL-CddbServer, use switch " + SPOOLER_REPORT_COMMAND_LINE_SWITCH + " to get an error-message");
                    } else try {
                        if (!containsSwitch(SPOOLER_CONTINUE_TABLES_COMMAND_LINE_SWITCH, args)) {
                            mySQLCDSpooler.initializeDataBase();
                            extractionReporter.flush();
                        }
                        if (containsSwitch(SPOOLER_LIMIT_ARTISTS_CACHE_PREFIX, args)) mySQLCDSpooler.setSpoolersArtistCacheSize(getIntValueFor(SPOOLER_LIMIT_ARTISTS_CACHE_PREFIX, args));
                        if (containsSwitch(SPOOLER_LIMIT_GENRES_CACHE_PREFIX, args)) mySQLCDSpooler.setSpoolersGenreCacheSize(getIntValueFor(SPOOLER_LIMIT_GENRES_CACHE_PREFIX, args));
                        SpoolingExtractor extractor = new SpoolingExtractor(sourceToExtractFrom, RawTextReader.UTF8, containsSwitch(EXTRACT_EASY_COMMAND_LINE_SWITCH, args), containsSwitch(EXTRACT_NO_DISC_WITHOUT_RELEASE_YEAR_SET_COMMAND_LINE_SWITCH, args), containsSwitch(EXTRACT_DUPLICATE_DISC_DEFINITIONS, args), mySQLCDSpooler);
                        extractor.startExtraction();
                        if (!containsSwitch(SKIP_INDEXING_DB_COMMAND_LINE_SWITCH, args)) {
                            extractionReporter.flush();
                            mySQLCDSpooler.createIndices();
                        }
                        System.out.println(extractor.getOperationSummary());
                        System.out.println(mySQLCDSpooler.getOperationSummary());
                        mySQLCDSpooler.close();
                        extractor.close();
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                    }
                }
            }
        }
        long discsFound = 0;
        MySQLConnector connector = null;
        boolean showTracks = containsSwitch(DB_QUERY_SHOW_TRACKS_COMMAND_LINE_SWITCH, args);
        if (containsSwitch(DB_QUERY_DUMP_SERVER_COMPLETELY_COMMAND_LINE_SWITCH, args)) {
            connector = getMySQLConnectorConfiguredFromCommandLine(args, MySQLConnector.TABLES_COUNT_UNSET, new CDAdapter(), new ExceptionReporter());
            if (connector != null) {
                System.out.print("Dumping ");
                System.out.print(statementGenerator.getDataBaseName());
                System.out.print(" containing ");
                System.out.print(connector.getNumberOfDiscsInDataBase());
                System.out.println(" entries");
                System.out.println();
                CDEnumeration e = connector.getCompactDiscs();
                while (e.hasMoreCDs()) {
                    dumpDisc(e.nextCD(), showTracks);
                    discsFound++;
                }
                System.out.println();
                System.out.println(discsFound + " discs found");
            }
        }
        if (containsSwitch(DB_QUERY_ARTIST_NAME_PREFIX, args)) {
            connector = getMySQLConnectorConfiguredFromCommandLine(args, MySQLConnector.TABLES_COUNT_UNSET, new CDAdapter(), new ExceptionReporter());
            String artist = getStringValueFor(DB_QUERY_ARTIST_NAME_PREFIX, args);
            if ((connector != null) && (artist != null)) {
                System.out.print("Querying ");
                System.out.print(statementGenerator.getDataBaseName());
                System.out.print(" (");
                System.out.print(connector.getNumberOfDiscsInDataBase());
                System.out.print(" entries) for discs by artists named ");
                System.out.println(artist);
                System.out.println();
                CDEnumeration e = connector.getCompactDiscsByArtist(new TextualDisciminator(MySQLStatementGenerator.ARTISTS_NAME_COLUMN_NAME, artist));
                while (e.hasMoreCDs()) {
                    dumpDisc(e.nextCD(), showTracks);
                    discsFound++;
                }
                System.out.println();
                System.out.println(discsFound + " discs found");
            }
        }
        if (containsSwitch(DB_QUERY_DISC_TITLE_PREFIX, args)) {
            connector = getMySQLConnectorConfiguredFromCommandLine(args, MySQLConnector.TABLES_COUNT_UNSET, new CDAdapter(), new ExceptionReporter());
            String title = getStringValueFor(DB_QUERY_DISC_TITLE_PREFIX, args);
            if ((connector != null) && (title != null)) {
                System.out.print("Querying ");
                System.out.print(statementGenerator.getDataBaseName());
                System.out.print(" (");
                System.out.print(connector.getNumberOfDiscsInDataBase());
                System.out.print(" entries) for discs titeled ");
                System.out.println(title);
                System.out.println();
                CDEnumeration e = connector.getCompactDiscsByTitle(new TextualDisciminator(MySQLStatementGenerator.DISCS_TITLE_COLUMN_NAME, title));
                while (e.hasMoreCDs()) {
                    dumpDisc(e.nextCD(), showTracks);
                    discsFound++;
                }
                System.out.println();
                System.out.println(discsFound + " discs found");
            }
        }
        if (containsSwitch(DB_QUERY_FREEDB_DISCID_PREFIX, args)) {
            connector = getMySQLConnectorConfiguredFromCommandLine(args, MySQLConnector.TABLES_COUNT_UNSET, new CDAdapter(), new ExceptionReporter());
            String freeDBDiscId = getStringValueFor(DB_QUERY_FREEDB_DISCID_PREFIX, args);
            if ((connector != null) && (freeDBDiscId != null)) {
                System.out.print("Querying ");
                System.out.print(statementGenerator.getDataBaseName());
                System.out.print(" (");
                System.out.print(connector.getNumberOfDiscsInDataBase());
                System.out.print(" entries) for id ");
                System.out.println(freeDBDiscId);
                System.out.println();
                try {
                    Long discId = new Long(Long.parseLong(freeDBDiscId, 16));
                    CDEnumeration e = connector.getCompactDiscsByFreeDBId(new NumericalDisciminator(MySQLStatementGenerator.DISCS_FREEDB_DISCID_COLUMN_NAME, discId.longValue(), discId.longValue()));
                    while (e.hasMoreCDs()) {
                        dumpDisc(e.nextCD(), showTracks);
                        discsFound++;
                    }
                    System.out.println();
                    System.out.println(discsFound + " discs found");
                } catch (NumberFormatException e) {
                }
            }
        }
    }
}
