class BackupThread extends Thread {
    public void measurePhaseResponse(double phase_shift, double sleepTime, int nAvg, BasicGraphData experGD) {
        runningThread = Thread.currentThread();
        double s_min = accSeqGlobal.getLength() + 10.;
        double s_max = -10.;
        for (DevTreeNode accSeqNode : rootBPMTreeNode.children) {
            for (DevTreeNode accNodeNode : accSeqNode.children) {
                if (accNodeNode.isOn == true) {
                    double s = accSeqGlobal.getPosition(accNodeNode.accNode);
                    if (s < s_min) s_min = s;
                    if (s > s_max) s_max = s;
                }
            }
        }
        for (DevTreeNode accSeqNode : rootRFTreeNode.children) {
            for (DevTreeNode accNodeNode : accSeqNode.children) {
                if (accNodeNode.isOn == true) {
                    double s = accSeqGlobal.getPosition(accNodeNode.accNode);
                    double L = accNodeNode.accNode.getLength();
                    if (s < s_min) s_min = s;
                    s = s + L;
                    if (s > s_max) s_max = s;
                }
            }
        }
        Vector<AcceleratorSeq> accSeq_slct_V = new Vector<AcceleratorSeq>();
        for (AcceleratorSeq accSeq : accSeqV) {
            double s = accSeqGlobal.getPosition(accSeq);
            double L = accSeq.getLength();
            if (s_min >= s && s_min < s + L) {
                accSeq_slct_V.add(accSeq);
            } else {
                if (s >= s_min && s < s_max) {
                    accSeq_slct_V.add(accSeq);
                }
            }
        }
        bpmNodeV = new Vector<DevTreeNode>();
        for (DevTreeNode accSeqNode : rootBPMTreeNode.children) {
            for (DevTreeNode accNodeNode : accSeqNode.children) {
                if (accNodeNode.isOn == true) bpmNodeV.add(accNodeNode);
            }
        }
        rfNodeV = new Vector<DevTreeNode>();
        for (DevTreeNode accSeqNode : rootRFTreeNode.children) {
            for (DevTreeNode accNodeNode : accSeqNode.children) {
                if (accNodeNode.isOn == true) rfNodeV.add(accNodeNode);
            }
        }
        if (rfNodeV.size() == 0) {
            messageTextLocal.setText("You have to specify RF Cavities! Stop.");
            return;
        }
        Vector<DevTreeNode> rfCalcNodeV = new Vector<DevTreeNode>();
        for (DevTreeNode accSeqNode : rootRFTreeNode.children) {
            if (accSeq_slct_V.contains(accSeqNode.accSeq)) {
                rfCalcNodeV.addAll(accSeqNode.children);
            }
        }
        double rfFreq_Init = 1.0e+6 * ((RfCavity) rfCalcNodeV.get(0).accNode).getCavFreq();
        for (DevTreeNode rfNode : rfCalcNodeV) {
            if (rfNode.isOn == true) {
                RfCavity rfCav = (RfCavity) rfNode.accNode;
                double freq = 1.0e+6 * rfCav.getCavFreq();
                rfNode.hashT.put("rffrequency", new Double(1.0e+6 * rfCav.getCavFreq()));
                try {
                    double livePhase = rfCav.channelSuite().getChannel("cavPhaseSet").getValDbl();
                    rfNode.hashT.put("livePhase", new Double(livePhase));
                } catch (ChannelException extp) {
                    messageTextLocal.setText("Cannot read from EPICS! " + rfCav.getId() + " Stop.");
                    return;
                }
            }
        }
        for (DevTreeNode bpmNode : bpmNodeV) {
            BPM bpm = (BPM) bpmNode.accNode;
            try {
                double phase = bpm.getPhaseAvg();
                bpmNode.hashT.put("initPhase", new Double(phase));
                bpmNode.hashT.put("newPhase", new Double(phase));
                bpmNode.hashT.put("n_measurements", new Integer(0));
                bpmNode.hashT.put("slopeSum", new Double(0));
                bpmNode.hashT.put("slopeSum2", new Double(0));
                double freq = 1.0e+6 * bpm.getBucket("bpm").getAttr("frequency").getDouble();
                double phaseCoeff = rfFreq_Init / freq;
                bpmNode.hashT.put("phaseCoeff", new Double(phaseCoeff));
            } catch (ChannelException extp) {
                messageTextLocal.setText("Cannot read from EPICS! " + bpm.getId() + " Stop.");
                return;
            }
        }
        for (int iM = 0; iM < nAvg; iM++) {
            for (int iST = 0; iST < 2; iST++) {
                int index = iST + 2 * iM + 1;
                int i_total = 2 * nAvg;
                messageTextLocal.setText("Measurement done " + index + " of " + i_total);
                for (DevTreeNode rfNode : rfCalcNodeV) {
                    if (rfNode.isOn == true) {
                        RfCavity rfCav = (RfCavity) rfNode.accNode;
                        try {
                            double livePhase = rfNode.hashT.get("livePhase").doubleValue();
                            rfCav.channelSuite().getChannel("cavPhaseSet").putVal(livePhase + iST * phase_shift);
                        } catch (ChannelException extp) {
                            messageTextLocal.setText("Cannot read from EPICS! " + rfCav.getId() + " Stop.");
                        }
                    }
                }
                try {
                    Thread.sleep((long) (1000 * sleepTime));
                } catch (InterruptedException expt) {
                    measureBPM(-1, phase_shift, experGD);
                    messageTextLocal.setText("The shaking was interrupted! Stop.");
                    resetRF_Phases(rfCalcNodeV);
                    return;
                }
                measureBPM(iST, phase_shift, experGD);
            }
        }
        resetRF_Phases(rfCalcNodeV);
        System.out.println("debug measurement done!");
    }
}
