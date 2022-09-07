class BackupThread extends Thread {
    public static void main(String[] args) throws Exception {
        IOUtil.setUpLogging();
        Preferences.setAppName("OBO2OBO");
        logger.info("Starting OBO2OBO");
        logger.info("OBO-Edit version = " + Preferences.getVersion());
        if (args.length == 0) printUsage(1);
        OBOFileAdapter.OBOAdapterConfiguration readConfig = new OBOFileAdapter.OBOAdapterConfiguration();
        readConfig.setBasicSave(false);
        readConfig.setAllowDangling(true);
        OBOFileAdapter.OBOAdapterConfiguration writeConfig = new OBOFileAdapter.OBOAdapterConfiguration();
        writeConfig.setBasicSave(false);
        writeConfig.setAllowDangling(true);
        boolean parseObsoleteComments = false;
        boolean writeObsoleteComments = false;
        boolean fixDbxrefs = false;
        boolean checkForSimilarDefs = false;
        LinkedList scripts = new LinkedList();
        String formatVersion = "OBO_1_2";
        for (int i = 0; i < args.length; i++) logger.info("args[" + i + "] = |" + args[i] + "|");
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-formatversion")) {
                if (i >= args.length - 1) printUsage(1);
                i++;
                formatVersion = args[i];
                if (!(formatVersion.equals("OBO_1_2") || formatVersion.equals("OBO_1_0"))) printUsage(1);
            } else if (args[i].equals("-parsecomments")) {
                parseObsoleteComments = true;
            } else if (args[i].equals("-allowdangling")) {
                logger.info("Please note: allowdangling is already set to true by default for both input and output files.");
            } else if (args[i].equals("-disallowdangling")) {
                readConfig.setAllowDangling(false);
            } else if (args[i].equals("-fixdbxrefs")) {
                fixDbxrefs = true;
            } else if (args[i].equals("-writecomments")) {
                writeObsoleteComments = true;
            } else if (args[i].equals("-lexcheck")) {
                checkForSimilarDefs = true;
            } else if (args[i].equals("-semanticparse")) {
                SemanticParser sp = new RegulationTermParser();
                if (args[i + 1].equals("-addsynonyms")) {
                    i++;
                    sp.useDefaultNamer();
                }
                readConfig.setSemanticParser(sp);
            } else if (args[i].equals("-runscript")) {
                if (i >= args.length - 1) printUsage(1);
                i++;
                String scriptFile = args[i];
                String script = IOUtil.readFile(scriptFile);
                ScriptWrapper wrapper = new ScriptWrapper();
                wrapper.setScript(script);
                for (i = i + 1; i < args.length; i++) {
                    if (args[i].equals(";")) {
                        break;
                    }
                    wrapper.getArgs().add(args[i]);
                }
                scripts.add(wrapper);
            } else if (args[i].equals("-o")) {
                if (i >= args.length - 1) printUsage(1);
                i++;
                OBOSerializationEngine.FilteredPath path = new OBOSerializationEngine.FilteredPath();
                path.setUseSessionReasoner(false);
                for (; i < args.length; i++) {
                    if (args[i].equals("-f")) {
                        if (i >= args.length - 1) printUsage(1);
                        i++;
                        String filterFile = args[i];
                        Filter filter = FilterUtil.loadFilter(filterFile);
                        path.setDoFilter(filter != null);
                        path.setObjectFilter(filter);
                    } else if (args[i].equals("-lf")) {
                        if (i >= args.length - 1) printUsage(1);
                        i++;
                        String filterFile = args[i];
                        Filter filter = FilterUtil.loadFilter(filterFile);
                        path.setDoLinkFilter(filter != null);
                        path.setLinkFilter(filter);
                    } else if (args[i].equals("-reasonerfactory")) {
                        if (i >= args.length - 1) printUsage(1);
                        i++;
                        path.setReasonerFactory((ReasonerFactory) Class.forName(args[i]).newInstance());
                    } else if (args[i].equals("-allowdangling")) {
                        path.setAllowDangling(true);
                    } else if (args[i].equals("-strictrootdetection")) {
                        path.setRootAlgorithm("STRICT");
                    } else if (args[i].equals("-saveimpliedlinks")) {
                        path.setSaveImplied(true);
                        path.setImpliedType(OBOSerializationEngine.SAVE_TRIMMED_LINKS);
                    } else if (args[i].equals("-saveallimpliedlinks")) {
                        path.setSaveImplied(true);
                        path.setImpliedType(OBOSerializationEngine.SAVE_ALL);
                    } else if (args[i].equals("-filterredundantlinks")) {
                        LinkFilterFactory lff = new LinkFilterFactory();
                        ObjectFilterFactory off = new ObjectFilterFactory();
                        LinkFilter lf = (LinkFilter) lff.createNewFilter();
                        ObjectFilter filter = (ObjectFilter) off.createNewFilter();
                        filter.setCriterion(new IsRedundantLinkCriterion());
                        lf.setFilter(filter);
                        path.setLinkFilter(lf);
                        path.setDoLinkFilter(true);
                    } else if (args[i].equals("-realizeimpliedlinks")) {
                        path.setAssertImpliedLinks(true);
                    } else if (args[i].equals("-p")) {
                        if (i >= args.length - 1) printUsage(1);
                        i++;
                        String prefilterProperty = args[i];
                        path.setPrefilterProperty(prefilterProperty);
                    } else {
                        path.setPath(args[i]);
                        break;
                    }
                }
                if (path.getPath() == null) printUsage(1); else writeConfig.getSaveRecords().add(path);
            } else if (args[i].equals("-?")) {
                printUsage(0);
            } else {
                readConfig.getReadPaths().add(args[i]);
            }
        }
        if (readConfig.getReadPaths().size() < 1) {
            logger.info("You must specify at least one file to load.");
            printUsage(1);
        }
        if (writeConfig.getSaveRecords().size() < 1) {
            if (scripts.size() == 0) {
                logger.error("You must specify at least one file to save.");
                printUsage(1);
            }
        }
        writeConfig.setSerializer(formatVersion);
        OBOSession session = convertFiles(readConfig, writeConfig, parseObsoleteComments, writeObsoleteComments, fixDbxrefs, scripts);
        if (checkForSimilarDefs) checkForSimilarDefs(session);
    }
}
