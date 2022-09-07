class BackupThread extends Thread {
    public void writeData(XmlDataAdaptor data) {
        messageTextLocal.setText(null);
        XmlDataAdaptor runnerDA = (XmlDataAdaptor) data.createChild("RUNNER_CNTRL");
        Vector psgV = getPowerSupplyGroups();
        for (int i = 0, n = psgV.size(); i < n; i++) {
            PowerSupplyGroup psg = (PowerSupplyGroup) psgV.get(i);
            XmlDataAdaptor psgDA = (XmlDataAdaptor) runnerDA.createChild("PowerSupplyGroup");
            psgDA.setValue("group_name", psg.getName());
            for (int j = 0, nj = psg.getPowerSupplyCyclers().size(); j < nj; j++) {
                PowerSupplyCycler psc = (PowerSupplyCycler) psg.getPowerSupplyCyclers().get(j);
                XmlDataAdaptor pscDA = (XmlDataAdaptor) psgDA.createChild("PowerSupplyCycler");
                XmlDataAdaptor pscSetPV_DA = (XmlDataAdaptor) pscDA.createChild("PV_Set");
                pscSetPV_DA.setValue("pv_name", psc.getChannelName());
                XmlDataAdaptor pscRbPV_DA = (XmlDataAdaptor) pscDA.createChild("PV_Rb");
                pscRbPV_DA.setValue("pv_name", psc.getChannelNameRB());
                XmlDataAdaptor pscMaxCurr_DA = (XmlDataAdaptor) pscDA.createChild("Max_I_Ampers");
                pscMaxCurr_DA.setValue("I", psc.getMaxCurrent());
                XmlDataAdaptor pscnCycl_DA = (XmlDataAdaptor) pscDA.createChild("Number_of_Cycles");
                pscnCycl_DA.setValue("n", psc.getnCycles());
                XmlDataAdaptor pscChangeRate_DA = (XmlDataAdaptor) pscDA.createChild("Rate_Amper_per_sec");
                pscChangeRate_DA.setValue("rate", psc.getChangeRate());
                XmlDataAdaptor pscMinCurrTime_DA = (XmlDataAdaptor) pscDA.createChild("MinI_Time_sec");
                pscMinCurrTime_DA.setValue("time", psc.getMinCurrTime());
                XmlDataAdaptor pscMaxCurrTime_DA = (XmlDataAdaptor) pscDA.createChild("MaxI_Time_sec");
                pscMaxCurrTime_DA.setValue("time", psc.getMaxCurrTime());
                XmlDataAdaptor pscActive_DA = (XmlDataAdaptor) pscDA.createChild("Active");
                pscActive_DA.setValue("isActive", psc.getActive());
            }
        }
    }
}
