class BackupThread extends Thread {
    protected static String[] getPVsToLog() {
        Accelerator accelerator = XMLDataManager.loadDefaultAccelerator();
        List<String> pvs = new ArrayList<String>();
        List<AcceleratorNode> bpms = accelerator.getAllNodesOfType(BPM.s_strType);
        Iterator<AcceleratorNode> bpmIter = bpms.iterator();
        while (bpmIter.hasNext()) {
            BPM bpm = (BPM) bpmIter.next();
            pvs.add(bpm.getChannel(BPM.X_AVG_HANDLE).channelName());
            pvs.add(bpm.getChannel(BPM.Y_AVG_HANDLE).channelName());
        }
        System.out.println("Found " + pvs.size() + " PVs: ");
        System.out.println(pvs);
        String[] pvArray = new String[pvs.size()];
        return pvs.toArray(pvArray);
    }
}
