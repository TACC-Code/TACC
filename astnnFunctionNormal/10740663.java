class BackupThread extends Thread {
    private void makeTreeListeners() {
        switchPVTreeListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String command = e.getActionCommand();
                PVTreeNode pvn = (PVTreeNode) e.getSource();
                boolean switchOnLocal = command.equals(PVTreeNode.SWITCHED_ON_COMMAND);
                PVTreeNode pvn_parent = (PVTreeNode) pvn.getParent();
                int index = -1;
                if (switchOnLocal) {
                    if (pvn_parent == scanPV_Node) {
                        scanPV_ShowState = true;
                        updateDataSetOnGraphPanels();
                    }
                    if (pvn_parent == scanPV_RB_Node) {
                        scanPV_RB_ShowState = true;
                        updateDataSetOnGraphPanels();
                    }
                    if (pvn_parent == measuredPVs_Node) {
                        index = pvn_parent.getIndex(pvn);
                        measuredValuesShowStateV.set(index, new Boolean(true));
                        MeasuredValue mv_tmp = (MeasuredValue) measuredValuesV.get(index);
                        scanController.removeMeasuredValue(mv_tmp);
                        scanController.addMeasuredValue(mv_tmp);
                        updateDataSetOnGraphPanels();
                    }
                    if (pvn_parent == validationPVs_Node) {
                        index = pvn_parent.getIndex(pvn);
                        scanController.removeValidationValue((MeasuredValue) validationValuesV.get(index));
                        scanController.addValidationValue((MeasuredValue) validationValuesV.get(index));
                    }
                } else {
                    if (pvn_parent == scanPV_Node) {
                        scanPV_ShowState = false;
                        updateDataSetOnGraphPanels();
                    }
                    if (pvn_parent == scanPV_RB_Node) {
                        scanPV_RB_ShowState = false;
                        updateDataSetOnGraphPanels();
                    }
                    if (pvn_parent == measuredPVs_Node) {
                        index = pvn_parent.getIndex(pvn);
                        measuredValuesShowStateV.set(index, new Boolean(false));
                        MeasuredValue mv_tmp = (MeasuredValue) measuredValuesV.get(index);
                        scanController.removeMeasuredValue(mv_tmp);
                        updateDataSetOnGraphPanels();
                    }
                    if (pvn_parent == validationPVs_Node) {
                        index = pvn_parent.getIndex(pvn);
                        scanController.removeValidationValue((MeasuredValue) validationValuesV.get(index));
                    }
                }
            }
        };
        createDeletePVTreeListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                PVTreeNode pvn = (PVTreeNode) e.getSource();
                PVTreeNode pvn_parent = (PVTreeNode) pvn.getParent();
                String command = e.getActionCommand();
                boolean bool_removePV = command.equals(PVTreeNode.REMOVE_PV_COMMAND);
                int index = -1;
                if (bool_removePV) {
                    if (pvn_parent == parameterPV_Node) {
                        parameterPV_Controller.setChannel(null);
                    }
                    if (pvn_parent == parameterPV_RB_Node) {
                        parameterPV_Controller.setChannelRB(null);
                    }
                    if (pvn_parent == scanPV_Node) {
                        scanVariable.setChannel(null);
                        scanPV_ShowState = false;
                        graphScan.setAxisNames("Scan PV Values", "Measured Values");
                        graphAnalysis.setAxisNames("Scan PV Values", "Measured Values");
                        for (int i = 0, n = measuredValuesV.size(); i < n; i++) {
                            MeasuredValue mv_tmp = (MeasuredValue) measuredValuesV.get(i);
                            mv_tmp.removeAllDataContainersNonRB();
                        }
                        updateDataSetOnGraphPanels();
                    }
                    if (pvn_parent == scanPV_RB_Node) {
                        scanVariable.setChannelRB(null);
                        scanPV_RB_ShowState = false;
                        for (int i = 0, n = measuredValuesV.size(); i < n; i++) {
                            MeasuredValue mv_tmp = (MeasuredValue) measuredValuesV.get(i);
                            mv_tmp.removeAllDataContainersRB();
                        }
                        updateDataSetOnGraphPanels();
                    }
                    if (pvn_parent == measuredPVs_Node) {
                        index = pvn_parent.getIndex(pvn);
                        MeasuredValue mv_tmp = (MeasuredValue) measuredValuesV.get(index);
                        scanController.removeMeasuredValue(mv_tmp);
                        MonitoredPV mpv_tmp = mv_tmp.getMonitoredPV();
                        MonitoredPV.removeMonitoredPV(mpv_tmp);
                        measuredValuesV.remove(index);
                        measuredValuesShowStateV.remove(index);
                        updateDataSetOnGraphPanels();
                        setColors(index);
                    }
                    if (pvn_parent == validationPVs_Node) {
                        index = pvn_parent.getIndex(pvn);
                        MeasuredValue mv_tmp = (MeasuredValue) validationValuesV.get(index);
                        scanController.removeValidationValue(mv_tmp);
                        MonitoredPV mpv_tmp = mv_tmp.getMonitoredPV();
                        MonitoredPV.removeMonitoredPV(mpv_tmp);
                        validationValuesV.remove(index);
                    }
                } else {
                    if (pvn_parent == parameterPV_Node) {
                        parameterPV_Controller.setChannel(pvn.getChannel());
                    }
                    if (pvn_parent == parameterPV_RB_Node) {
                        parameterPV_Controller.setChannelRB(pvn.getChannel());
                    }
                    if (pvn_parent == scanPV_Node) {
                        scanVariable.setChannel(pvn.getChannel());
                        scanPV_ShowState = true;
                        graphScan.setAxisNames("Scan PV : " + pvn.getChannel().getId(), "Measured Values");
                        graphAnalysis.setAxisNames("Scan PV : " + pvn.getChannel().getId(), "Measured Values");
                        updateDataSetOnGraphPanels();
                    }
                    if (pvn_parent == scanPV_RB_Node) {
                        scanVariable.setChannelRB(pvn.getChannel());
                        scanPV_RB_ShowState = true;
                        updateDataSetOnGraphPanels();
                    }
                    if (pvn_parent == measuredPVs_Node) {
                        index = pvn_parent.getIndex(pvn);
                        MeasuredValue mv_tmp = new MeasuredValue("measured_pv_" + monitoredPV_Count);
                        monitoredPV_Count++;
                        mv_tmp.setChannel(pvn.getChannel());
                        measuredValuesV.add(mv_tmp);
                        measuredValuesShowStateV.add(new Boolean(true));
                        scanController.addMeasuredValue(mv_tmp);
                        setColors(-1);
                    }
                    if (pvn_parent == validationPVs_Node) {
                        index = pvn_parent.getIndex(pvn);
                        MeasuredValue mv_tmp = new MeasuredValue("measured_pv_" + monitoredPV_Count);
                        monitoredPV_Count++;
                        mv_tmp.setChannel(pvn.getChannel());
                        validationValuesV.add(mv_tmp);
                        scanController.addValidationValue(mv_tmp);
                    }
                }
            }
        };
        renamePVTreeListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                PVTreeNode pvn = (PVTreeNode) e.getSource();
                PVTreeNode pvn_parent = (PVTreeNode) pvn.getParent();
                int index = -1;
                if (pvn_parent == parameterPV_Node) {
                    parameterPV_Controller.setChannel(pvn.getChannel());
                }
                if (pvn_parent == parameterPV_RB_Node) {
                    parameterPV_Controller.setChannelRB(pvn.getChannel());
                }
                if (pvn_parent == scanPV_Node) {
                    scanVariable.setChannel(pvn.getChannel());
                    graphScan.setAxisNames("Scan PV : " + pvn.getChannel().getId(), "Measured Values");
                    graphAnalysis.setAxisNames("Scan PV : " + pvn.getChannel().getId(), "Measured Values");
                    graphScan.refreshGraphJPanel();
                    graphAnalysis.refreshGraphJPanel();
                }
                if (pvn_parent == scanPV_RB_Node) {
                    scanVariable.setChannelRB(pvn.getChannel());
                }
                if (pvn_parent == measuredPVs_Node) {
                    index = pvn_parent.getIndex(pvn);
                    MeasuredValue mv_tmp = (MeasuredValue) measuredValuesV.get(index);
                    MonitoredPV mpv_tmp = mv_tmp.getMonitoredPV();
                    mpv_tmp.setChannel(pvn.getChannel());
                }
                if (pvn_parent == validationPVs_Node) {
                    index = pvn_parent.getIndex(pvn);
                    MeasuredValue mv_tmp = (MeasuredValue) validationValuesV.get(index);
                    MonitoredPV mpv_tmp = mv_tmp.getMonitoredPV();
                    mpv_tmp.setChannel(pvn.getChannel());
                }
            }
        };
    }
}
