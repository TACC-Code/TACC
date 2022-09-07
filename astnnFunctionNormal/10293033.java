class BackupThread extends Thread {
    public void execute(IProcessData processData) throws ServiceException {
        org.jcvi.vics.compute.api.ComputeBeanRemote computeBean = EJBFactory.getRemoteComputeBean();
        try {
            ProcessDataHelper.getLoggerForTask(processData, this.getClass());
            this.task = ProcessDataHelper.getTask(processData);
            String sessionName = ProcessDataHelper.getSessionRelativePath(processData);
            Set<Node> inputNodes = task.getInputNodes();
            Node inputNode;
            if (null != inputNodes && null != inputNodes.iterator() && inputNodes.iterator().hasNext()) {
                inputNode = inputNodes.iterator().next();
            } else {
                throw new ServiceException("Do not have user sequence for Blast-Frv.  Task=" + task.getObjectId());
            }
            StringBuffer tmpSequence = new StringBuffer();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i <= 1000; i++) {
                builder.append('N');
            }
            String gaps = builder.toString();
            if (inputNode instanceof FastaFileNode) {
                FastaFileNode tmpNode = (FastaFileNode) inputNode;
                if (!SequenceType.NUCLEOTIDE.equalsIgnoreCase(tmpNode.getSequenceType())) {
                    throw new ServiceException("Do not have valid user sequence type for Blast-Frv.  Task=" + task.getObjectId());
                }
                FileInputStream fis = new FileInputStream(new File(tmpNode.getFastaFilePath()));
                FASTAFileTokenizer tok = new FASTAFileTokenizer(fis.getChannel());
                tok.nextFASTAEntry(true);
                String tmpEntrySeq = tok.getFastaEntry();
                long numEntries = 0;
                long seqCount = 0;
                while (null != tmpEntrySeq && !"".equals(tmpEntrySeq)) {
                    String sequenceText = tmpEntrySeq.substring(tmpEntrySeq.indexOf("\n"));
                    sequenceText = sequenceText.replace("\n", "");
                    sequenceText = sequenceText.replace(" ", "");
                    sequenceText = sequenceText.trim();
                    tmpSequence.append(sequenceText);
                    numEntries++;
                    seqCount += sequenceText.length();
                    tok.nextFASTAEntry(true);
                    tmpEntrySeq = tok.getFastaEntry();
                    if (null != tmpEntrySeq && !"".equals(tmpEntrySeq)) {
                        tmpSequence.append(gaps);
                    }
                }
                System.out.println("FastaFileNode: Parsed " + numEntries + " entries for a total sequence count of " + seqCount + ":" + tmpSequence.length());
            }
            String finalSeq = FastaUtil.formatFasta("FRVBlast", tmpSequence.toString(), 60);
            FastaFileNode ffn = new FastaFileNode(task.getOwner(), null, inputNode.getName(), inputNode.getName(), Node.VISIBILITY_PRIVATE_DEPRECATED, FastaFileNode.NUCLEOTIDE, 1, sessionName);
            ffn.setLength((long) tmpSequence.length());
            ffn = (FastaFileNode) computeBean.saveOrUpdateNode(ffn);
            if (null == ffn || null == ffn.getObjectId()) {
                System.out.println("The FastaFileNode was not properly saved! Ensure computeserver.ejb.service property is correct!");
            }
            File ffnDir = new File(ffn.getDirectoryPath());
            boolean dirsCreated = ffnDir.mkdirs();
            if (!dirsCreated) {
                throw new ServiceException("Unable to create dirs for the UserBlastFrvGridService");
            }
            FileWriter fos = new FileWriter(ffn.getDirectoryPath() + File.separator + FastaFileNode.NUCLEOTIDE + "." + FastaFileNode.TAG_FASTA);
            fos.append(finalSeq);
            fos.close();
            BlastNTask blastNTask = new BlastNTask();
            blastNTask.setParameter(Task.PARAM_project, task.getParameter(Task.PARAM_project));
            blastNTask.setJobName("FRV Blast of " + ffn.getName());
            blastNTask.setParameter(BlastNTask.PARAM_query, ffn.getObjectId().toString());
            blastNTask.setParentTaskId(task.getObjectId());
            blastNTask.setParameter(BlastNTask.PARAM_subjectDatabases, "1054893807616655712");
            blastNTask.setParameter(BlastNTask.PARAM_databaseAlignments, "10000");
            blastNTask.setParameter(BlastNTask.PARAM_lowerCaseFiltering, "true");
            blastNTask.setParameter(BlastNTask.PARAM_evalue, "-4");
            blastNTask.setParameter(BlastNTask.PARAM_mismatchPenalty, "-5");
            blastNTask.setParameter(BlastNTask.PARAM_databaseDescriptions, "5");
            blastNTask.setParameter(BlastNTask.PARAM_gappedAlignmentDropoff, "150");
            blastNTask.setParameter(BlastNTask.PARAM_matchReward, "4");
            blastNTask.setParameter(BlastNTask.PARAM_filter, "m L");
            blastNTask.setOwner(task.getOwner());
            blastNTask = (BlastNTask) computeBean.saveOrUpdateTask(blastNTask);
            computeBean.saveEvent(task.getObjectId(), Event.RUNNING_EVENT, Event.RUNNING_EVENT, new Date());
            computeBean.submitJob("FRVBlast", blastNTask.getObjectId());
            String status = waitAndVerifyCompletion(blastNTask.getObjectId());
            if (!"completed".equals(status)) {
                System.out.println("\n\n\nERROR: the blast job has not actually completed!\nStatus is " + status);
                throw new ServiceException("Error running the UserBlastFrvGridService FRVBlast");
            }
            BlastResultFileNode blastOutputNode = computeBean.getBlastResultFileNodeByTaskId(blastNTask.getObjectId());
            HashSet<BlastResultFileNode> rvRtInputNodes = new HashSet<BlastResultFileNode>();
            rvRtInputNodes.add(blastOutputNode);
            RecruitmentViewerRecruitmentTask recruitmentTask = new RecruitmentViewerRecruitmentTask(null, null, rvRtInputNodes, task.getOwner(), new ArrayList(), null, "All Metagenomic Sequence Reads (N)", inputNode.getName(), (long) tmpSequence.length(), null);
            recruitmentTask.setParameter(RecruitmentViewerTask.BLAST_DATABASE_IDS, "1054893807616655712");
            recruitmentTask.setParentTaskId(task.getObjectId());
            recruitmentTask = (RecruitmentViewerRecruitmentTask) computeBean.saveOrUpdateTask(recruitmentTask);
            computeBean.saveEvent(task.getObjectId(), Event.RECRUITING_EVENT, Event.RECRUITING_EVENT, new Date());
            computeBean.submitJob("FrvDataRecruitment", recruitmentTask.getObjectId());
            RecruitmentFileNode recruitmentFileNode = (RecruitmentFileNode) computeBean.getResultNodeByTaskId(recruitmentTask.getObjectId());
            HashSet<RecruitmentFileNode> filterInputNodes = new HashSet<RecruitmentFileNode>();
            filterInputNodes.add(recruitmentFileNode);
            RecruitmentViewerFilterDataTask filterDataTask = new RecruitmentViewerFilterDataTask(filterInputNodes, task.getOwner(), new ArrayList(), recruitmentTask.getTaskParameterSet(), recruitmentTask.getParameter(RecruitmentViewerTask.SUBJECT), recruitmentTask.getParameter(RecruitmentViewerTask.QUERY), 0l, 50, 100, 0.0, Double.parseDouble(recruitmentTask.getParameter(RecruitmentViewerRecruitmentTask.GENOME_SIZE)), computeBean.getAllSampleNamesAsList(), RecruitmentViewerFilterDataTask.INITIAL_MATE_BITS, null, null, RecruitmentViewerFilterDataTask.COLORIZATION_SAMPLE);
            filterDataTask.setParentTaskId(task.getObjectId());
            filterDataTask.setParameter(Task.PARAM_project, task.getParameter(Task.PARAM_project));
            filterDataTask = (RecruitmentViewerFilterDataTask) computeBean.saveOrUpdateTask(filterDataTask);
            computeBean.saveEvent(task.getObjectId(), Event.GENERATING_IMAGES_EVENT, Event.GENERATING_IMAGES_EVENT, new Date());
            computeBean.submitJob("FrvNovelGrid", filterDataTask.getObjectId());
            String imageStatus = waitAndVerifyCompletion(filterDataTask.getObjectId());
            if (!"completed".equals(imageStatus)) {
                System.out.println("\n\n\nERROR: the filter job has not actually completed!\nStatus is " + status);
                throw new ServiceException("Error running the UserBlastFrvGridService FrvNovelGrid");
            }
            RecruitmentResultFileNode rrfn = (RecruitmentResultFileNode) computeBean.getResultNodeByTaskId(filterDataTask.getObjectId());
            RecruitmentDataFastaBuilderTask fastaTask = new RecruitmentDataFastaBuilderTask(rrfn.getObjectId().toString());
            fastaTask.setOwner(task.getOwner());
            fastaTask = (RecruitmentDataFastaBuilderTask) computeBean.saveOrUpdateTask(fastaTask);
            computeBean.saveEvent(task.getObjectId(), Event.FASTA_GENERATION_EVENT, Event.FASTA_GENERATION_EVENT, new Date());
            computeBean.submitJob("FrvDataFastaNonGrid", fastaTask.getObjectId());
        } catch (Exception e) {
            System.out.println("\n\n\nError generating the FRV data for user " + task.getOwner() + ", task=" + task.getObjectId() + "\nERROR:" + e.getMessage());
            try {
                computeBean.saveEvent(task.getObjectId(), Event.ERROR_EVENT, "Error executing the FRV pipeline", new Date());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            throw new ServiceException(e);
        }
    }
}
