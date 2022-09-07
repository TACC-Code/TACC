class BackupThread extends Thread {
    public static void main(String args[]) {
        paraProc(args);
        CanonicalGFF cgff = new CanonicalGFF(gffFilename);
        CanonicalGFF intronCGFF = Util.getIntronicCGFF(cgff);
        Map geneIntronCntMap = new TreeMap();
        Map geneIntronIcnsMap = new TreeMap();
        for (Iterator mriIterator = mappingFilenameMethodMap.entrySet().iterator(); mriIterator.hasNext(); ) {
            Map.Entry entry = (Entry) mriIterator.next();
            String mappingFilename = (String) entry.getKey();
            String mappingMethod = (String) entry.getValue();
            int mappedReadCnt = 0;
            int processedLines = 0;
            for (MappingResultIterator mappingResultIterator = Util.getMRIinstance(mappingFilename, mappingMethodMap, mappingMethod); mappingResultIterator.hasNext(); ) {
                mappedReadCnt++;
                ArrayList mappingRecords = (ArrayList) mappingResultIterator.next();
                processedLines += mappingRecords.size();
                if (mappingResultIterator.getBestIdentity() < identityCutoff) continue;
                ArrayList acceptedRecords = new ArrayList();
                for (int i = 0; i < mappingRecords.size(); i++) {
                    AlignmentRecord record = (AlignmentRecord) mappingRecords.get(i);
                    if (record.identity >= mappingResultIterator.getBestIdentity()) {
                        if (joinFactor > 0) record.nearbyJoin(joinFactor);
                        acceptedRecords.add(record);
                    }
                }
                if (acceptedRecords.size() > 1) continue;
                AlignmentRecord record = (AlignmentRecord) acceptedRecords.get(0);
                if (record.numBlocks > 1) continue;
                Set hitGenes = cgff.getRelatedGenes(record.chr, record.getMappingIntervals(), false, false, minimumOverlap, true);
                if (hitGenes.size() > 1) continue;
                hitGenes = intronCGFF.getRelatedGenes(record.chr, record.getMappingIntervals(), true, false, minimumOverlap, true);
                if (hitGenes.size() != 1) continue;
                Interval alignmentInterval = (Interval) record.getMappingIntervals().iterator().next();
                GenomeInterval geneRegion = (GenomeInterval) hitGenes.iterator().next();
                String geneID = (String) geneRegion.getUserObject();
                Set intronRegions = (Set) intronCGFF.geneExonRegionMap.get(geneID);
                int intronNo = 0;
                Iterator intronIterator = intronRegions.iterator();
                for (; intronIterator.hasNext(); ) {
                    Interval intron = (Interval) intronIterator.next();
                    intronNo++;
                    if (intron.intersect(alignmentInterval, minimumOverlap) == false) continue;
                    Map intronCntMap;
                    if (geneIntronCntMap.containsKey(geneID)) {
                        intronCntMap = (Map) geneIntronCntMap.get(geneID);
                    } else {
                        intronCntMap = new TreeMap();
                        geneIntronCntMap.put(geneID, intronCntMap);
                    }
                    if (intronCntMap.containsKey(intronNo)) {
                        int val = ((Integer) intronCntMap.get(intronNo)).intValue();
                        intronCntMap.put(intronNo, val + 1);
                    } else {
                        intronCntMap.put(intronNo, 1);
                    }
                    Map intronIcnsMap;
                    if (geneIntronIcnsMap.containsKey(geneID)) {
                        intronIcnsMap = (Map) geneIntronIcnsMap.get(geneID);
                    } else {
                        intronIcnsMap = new TreeMap();
                        geneIntronIcnsMap.put(geneID, intronIcnsMap);
                    }
                    Set icns;
                    if (intronIcnsMap.containsKey(intronNo)) {
                        icns = (Set) intronIcnsMap.get(intronNo);
                    } else {
                        icns = new TreeSet();
                        intronIcnsMap.put(intronNo, icns);
                    }
                    IntervalCoverageNode thisIcn = new IntervalCoverageNode(alignmentInterval.getStart(), alignmentInterval.getStop(), mappingResultIterator.getReadID());
                    Set overlapIcns = new HashSet();
                    for (Iterator icnIterator = icns.iterator(); icnIterator.hasNext(); ) {
                        IntervalCoverageNode otherIcn = (IntervalCoverageNode) icnIterator.next();
                        if (thisIcn.intersect(otherIcn)) overlapIcns.add(otherIcn);
                    }
                    if (overlapIcns.size() == 0) {
                        icns.add(thisIcn);
                    } else {
                        overlapIcns.add(thisIcn);
                        IntervalCoverageNode newIcn = thisIcn.combine(overlapIcns);
                        icns.removeAll(overlapIcns);
                        icns.add(newIcn);
                    }
                }
            }
            System.out.println(mappedReadCnt + " mapped reads (" + processedLines + " lines) in " + mappingFilename);
        }
        try {
            FileWriter fw = new FileWriter(outFilename);
            fw.write("#geneID" + "\t" + "intronNo" + "\t" + "iStart" + "\t" + "iStop" + "\t" + "read" + "\t" + "rStart" + "\t" + "rStop" + "\t" + "array" + "\n");
            for (Iterator geneIterator = geneIntronIcnsMap.keySet().iterator(); geneIterator.hasNext(); ) {
                Object geneID = geneIterator.next();
                Set intronRegions = (Set) intronCGFF.geneExonRegionMap.get(geneID);
                Interval[] intronRegionArray = (Interval[]) intronRegions.toArray(new Interval[intronRegions.size()]);
                Map intronIcnsMap = (Map) geneIntronIcnsMap.get(geneID);
                Map intronCntMap = (Map) geneIntronCntMap.get(geneID);
                for (Iterator intronIterator = intronIcnsMap.keySet().iterator(); intronIterator.hasNext(); ) {
                    int intronNo = ((Integer) intronIterator.next()).intValue();
                    Set icnSet = (Set) intronIcnsMap.get(intronNo);
                    Interval intronInterval = intronRegionArray[intronNo - 1];
                    for (Iterator icnIterator = icnSet.iterator(); icnIterator.hasNext(); ) {
                        IntervalCoverageNode icn = (IntervalCoverageNode) icnIterator.next();
                        fw.write(geneID + "\t" + intronNo + "\t" + intronInterval.getStart() + "\t" + intronInterval.getStop() + "\t" + intronCntMap.get(intronNo) + "\t" + icn.getStart() + "\t" + icn.getStop() + "\t" + Arrays.toString(icn.getCoverageArray()) + "\n");
                    }
                }
            }
            fw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
