class BackupThread extends Thread {
    public ScanDocument2D() {
        ACTIVE_PANEL = SCAN_PANEL;
        rootParameterPV_Node.setPVNamesAllowed(false);
        parameterPV_Node.setPVNamesAllowed(true);
        parameterPV_RB_Node.setPVNamesAllowed(true);
        rootScanPV_Node.setPVNamesAllowed(false);
        scanPV_Node.setPVNamesAllowed(true);
        scanPV_RB_Node.setPVNamesAllowed(true);
        measuredPVs_Node.setPVNamesAllowed(true);
        validationPVs_Node.setPVNamesAllowed(true);
        parameterPV_Node.setPVNumberLimit(1);
        parameterPV_RB_Node.setPVNumberLimit(1);
        parameterPV_Node.setCheckBoxVisible(false);
        parameterPV_RB_Node.setCheckBoxVisible(false);
        scanPV_Node.setPVNumberLimit(1);
        scanPV_RB_Node.setPVNumberLimit(1);
        rootParameterPV_Node.add(parameterPV_Node);
        rootParameterPV_Node.add(parameterPV_RB_Node);
        rootScanPV_Node.add(scanPV_Node);
        rootScanPV_Node.add(scanPV_RB_Node);
        root_Node.add(rootParameterPV_Node);
        root_Node.add(rootScanPV_Node);
        root_Node.add(measuredPVs_Node);
        root_Node.add(validationPVs_Node);
        pvsSelector = new PVsSelector(root_Node);
        pvsSelector.removeMessageTextField();
        makeTreeListeners();
        scanPV_Node.setSwitchedOnOffListener(switchPVTreeListener);
        scanPV_RB_Node.setSwitchedOnOffListener(switchPVTreeListener);
        measuredPVs_Node.setSwitchedOnOffListener(switchPVTreeListener);
        validationPVs_Node.setSwitchedOnOffListener(switchPVTreeListener);
        parameterPV_Node.setCreateRemoveListener(createDeletePVTreeListener);
        parameterPV_RB_Node.setCreateRemoveListener(createDeletePVTreeListener);
        scanPV_Node.setCreateRemoveListener(createDeletePVTreeListener);
        scanPV_RB_Node.setCreateRemoveListener(createDeletePVTreeListener);
        measuredPVs_Node.setCreateRemoveListener(createDeletePVTreeListener);
        validationPVs_Node.setCreateRemoveListener(createDeletePVTreeListener);
        parameterPV_Node.setRenameListener(renamePVTreeListener);
        parameterPV_RB_Node.setRenameListener(renamePVTreeListener);
        scanPV_Node.setRenameListener(renamePVTreeListener);
        scanPV_RB_Node.setRenameListener(renamePVTreeListener);
        measuredPVs_Node.setRenameListener(renamePVTreeListener);
        validationPVs_Node.setRenameListener(renamePVTreeListener);
        scanVariableParameter = new ScanVariable("param_var_" + monitoredPV_Count, "param_var_RB_" + (monitoredPV_Count + 1));
        parameterPV_Controller = new ParameterPV_Controller(scanVariableParameter);
        paramPV_ON = true;
        scanVariable = new ScanVariable("scan_var_" + monitoredPV_Count, "scan_var_RB_" + (monitoredPV_Count + 1));
        monitoredPV_Count++;
        monitoredPV_Count++;
        scanController.setScanVariable(scanVariable);
        scanController.setParamVariable(scanVariableParameter);
        scanController.setAvgController(avgCntr);
        scanController.setValidationController(vldCntr);
        scanController.getUnitsLabel().setText(" [a.u.]");
        scanController.getParamUnitsLabel().setText(" [a.u.]");
        scanController.setParamPhaseScanButtonVisible(true);
        scanController.setValuePhaseScanButtonVisible(true);
        scanController.addNewSetOfDataListener(new ActionListener() {

            public void actionPerformed(ActionEvent evn) {
                String paramPV_string = "";
                String scanPV_string = "";
                String scanPV_RB_string = "";
                String measurePV_string = "";
                String legend_string = "";
                String legend_string_RB = "";
                Double paramValue = new Double(scanController.getParamValue());
                Double paramValueRB = new Double(scanController.getParamValueRB());
                if (paramPV_ON && parameterPV_Controller.getChannel() != null) {
                    paramPV_string = paramPV_string + " par.PV : " + parameterPV_Controller.getChannel().getId() + "=" + parameterPV_Controller.getValueAsString();
                    paramValue = new Double(parameterPV_Controller.getValue());
                    if (parameterPV_Controller.getChannelRB() != null) {
                        paramValueRB = new Double(parameterPV_Controller.getValueRB());
                    }
                } else {
                    paramPV_string = paramPV_string + " param.= " + paramValue;
                }
                if (scanVariable.getChannel() != null) {
                    scanPV_string = "xPV=" + scanVariable.getChannel().getId();
                }
                if (scanVariable.getChannelRB() != null) {
                    scanPV_RB_string = "xPV=" + scanVariable.getChannelRB().getId();
                }
                for (int i = 0, n = measuredValuesV.size(); i < n; i++) {
                    if (((Boolean) measuredValuesShowStateV.get(i)).booleanValue()) {
                        MeasuredValue mv_tmp = (MeasuredValue) measuredValuesV.get(i);
                        BasicGraphData gd = mv_tmp.getDataContainer();
                        if (mv_tmp.getChannel() != null) {
                            measurePV_string = "yPV=" + mv_tmp.getChannel().getId();
                        }
                        if (useTimeStampButton.isSelected()) {
                            legend_string = dateAndTime.getTime();
                        } else {
                            legend_string = "";
                        }
                        legend_string_RB = legend_string;
                        legend_string = legend_string + " " + scanPV_string + " " + measurePV_string + paramPV_string + " ";
                        if (gd != null) {
                            gd.setGraphProperty(graphScan.getLegendKeyString(), legend_string);
                            if (paramValue != null) {
                                gd.setGraphProperty("PARAMETER_VALUE", paramValue);
                            }
                            if (paramValueRB != null) {
                                gd.setGraphProperty("PARAMETER_VALUE_RB", paramValueRB);
                            }
                        }
                        legend_string_RB = legend_string_RB + " " + scanPV_RB_string + " " + measurePV_string + paramPV_string + " ";
                        if (scanVariable.getChannelRB() != null) {
                            gd = mv_tmp.getDataContainerRB();
                            if (gd != null) {
                                gd.setGraphProperty(graphScan.getLegendKeyString(), legend_string_RB);
                                if (paramValue != null) {
                                    gd.setGraphProperty("PARAMETER_VALUE", paramValue);
                                }
                                if (paramValueRB != null) {
                                    gd.setGraphProperty("PARAMETER_VALUE_RB", paramValueRB);
                                }
                            }
                        }
                    }
                }
                updateDataSetOnGraphPanels();
            }
        });
        scanController.addNewPointOfDataListener(new ActionListener() {

            public void actionPerformed(ActionEvent evn) {
                graphScan.refreshGraphJPanel();
                graphAnalysis.refreshGraphJPanel();
            }
        });
        SimpleChartPopupMenu.addPopupMenuTo(graphScan);
        SimpleChartPopupMenu.addPopupMenuTo(graphAnalysis);
        graphScan.setOffScreenImageDrawing(true);
        graphAnalysis.setOffScreenImageDrawing(true);
        graphScan.setName("SCAN : Measured Values vs. Scan PV's Values");
        graphAnalysis.setName("ANALYSIS : Measured Values vs. Scan PV's Values");
        graphScan.setAxisNames("Scan PV Values", "Measured Values");
        graphAnalysis.setAxisNames("Scan PV Values", "Measured Values");
        graphScan.setGraphBackGroundColor(Color.white);
        graphAnalysis.setGraphBackGroundColor(Color.white);
        analysisConfig = XmlDataAdaptor.newEmptyDocumentAdaptor().createChild(analysisConfig_SR);
        analysisConfig.createChild("MANAGEMENT");
        analysisConfig.createChild("FIND_MIN_MAX");
        analysisConfig.createChild("POLYNOMIAL_FITTING");
        analysisConfig.createChild("INTERSECTION_FINDING");
        makeScanPanel();
        makeAnalysisPanel();
        makeSelectionPVsPanel();
        makePreferencesPanel();
        makePredefinedConfigurationsPanel();
    }
}
