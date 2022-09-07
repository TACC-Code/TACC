class BackupThread extends Thread {
    public Scenario setModelSource(AcceleratorSeq seq, Scenario scenario) {
        myAccSeq = seq;
        AndTypeQualifier atq = new AndTypeQualifier();
        atq.and("Q");
        OrTypeQualifier otq1 = new OrTypeQualifier();
        otq1.or("DCH");
        otq1.or("DCV");
        OrTypeQualifier otq = new OrTypeQualifier();
        otq.or("PMQH");
        otq.or("PMQV");
        NotTypeQualifier ntq = new NotTypeQualifier(otq);
        otq1.or(atq);
        List<AcceleratorNode> allMags = seq.getNodesWithQualifier(otq1);
        List<AcceleratorNode> mags = AcceleratorSeq.filterNodesByStatus(allMags, true);
        for (int i = 0; i < mags.size(); i++) {
            Electromagnet quad = (Electromagnet) mags.get(i);
            String pvName = "";
            double val = quad.getDesignField();
            double pol = quad.getPolarity();
            if (quad.useFieldReadback()) {
                Channel chan = quad.getChannel(Electromagnet.FIELD_RB_HANDLE);
                pvName = chan.channelName();
                if (qPVMap.containsKey(pvName)) {
                    val = qPVMap.get(pvName).doubleValue();
                    val = val * pol;
                } else {
                    Channel chan2 = quad.getMainSupply().getChannel("psFieldRB");
                    String pvName2 = chan2.channelName();
                    if (qPSPVMap.containsKey(pvName2)) {
                        val = qPSPVMap.get(pvName2).doubleValue();
                        val = val * pol;
                    } else {
                        chan2 = quad.getMainSupply().getChannel("fieldSet");
                        pvName2 = chan2.channelName();
                        if (qPSPVMap.containsKey(pvName2)) {
                            val = qPSPVMap.get(pvName2).doubleValue();
                            val = val * pol;
                        } else System.out.println(pvName2 + " has no value");
                    }
                }
            } else {
                Channel chan = quad.getMainSupply().getChannel(MagnetMainSupply.FIELD_SET_HANDLE);
                pvName = chan.channelName();
                if (qPVMap.containsKey(pvName)) {
                    val = qPVMap.get(pvName).doubleValue();
                    val = val * pol;
                    if (quad instanceof TrimmedQuadrupole) {
                        Channel chan1 = ((TrimmedQuadrupole) quad).getTrimSupply().getChannel(MagnetTrimSupply.FIELD_SET_HANDLE);
                        String pvName1 = chan1.channelName();
                        if (qPVMap.containsKey(pvName1)) {
                            double trimVal = Math.abs(qPVMap.get(pvName1).doubleValue());
                            trimVal = chan1.getValueTransform().convertFromRaw(ArrayValue.doubleStore(trimVal)).doubleValue();
                            if (pvName1.indexOf("ShntC") > -1) {
                                val = val - trimVal;
                            } else {
                                val = val + trimVal;
                            }
                        }
                    }
                } else {
                    pvName = quad.getChannel(Electromagnet.FIELD_RB_HANDLE).channelName();
                    if (qPVMap.containsKey(pvName)) {
                        val = qPVMap.get(pvName).doubleValue();
                    }
                }
            }
            scenario.setModelInput(quad, ElectromagnetPropertyAccessor.PROPERTY_FIELD, val);
        }
        try {
            scenario.resync();
        } catch (SynchronizationException e) {
            System.out.println(e);
        }
        return scenario;
    }
}
