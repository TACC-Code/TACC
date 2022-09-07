class BackupThread extends Thread {
    public void readScanDocument(URL url) {
        XmlDataAdaptor readAdp = null;
        readAdp = XmlDataAdaptor.adaptorForUrl(url, false);
        if (readAdp != null) {
            XmlDataAdaptor scan2D_Adaptor = readAdp.childAdaptor(dataRootName_SR);
            XmlDataAdaptor params_scan2D = scan2D_Adaptor.childAdaptor(paramsName_SR);
            XmlDataAdaptor paramPV_scan2D = scan2D_Adaptor.childAdaptor(paramPV_SR);
            XmlDataAdaptor scanPV_scan2D = scan2D_Adaptor.childAdaptor(scanPV_SR);
            XmlDataAdaptor measurePVs_scan2D = scan2D_Adaptor.childAdaptor(measurePVs_SR);
            XmlDataAdaptor validationPVs_scan2D = scan2D_Adaptor.childAdaptor(validationPVs_SR);
            XmlDataAdaptor tmp_analysisConfig = scan2D_Adaptor.childAdaptor(analysisConfig_SR);
            if (tmp_analysisConfig != null) {
                analysisConfig = tmp_analysisConfig;
            } else {
                analysisConfig = XmlDataAdaptor.newEmptyDocumentAdaptor().createChild(analysisConfig_SR);
                analysisConfig.createChild("MANAGEMENT");
                analysisConfig.createChild("FIND_MIN_MAX");
                analysisConfig.createChild("POLYNOMIAL_FITTING");
                analysisConfig.createChild("INTERSECTION_FINDING");
            }
            setTitle(scan2D_Adaptor.stringValue("title"));
            XmlDataAdaptor params_font = params_scan2D.childAdaptor("font");
            globalFont = new Font(params_font.stringValue("name"), params_font.intValue("style"), params_font.intValue("size"));
            XmlDataAdaptor params_scan_panel_title = params_scan2D.childAdaptor("scan_panel_title");
            if (params_scan_panel_title != null && params_scan_panel_title.stringValue("title") != null) {
                scanController.setTitle(params_scan_panel_title.stringValue("title"));
            } else {
                scanController.setTitle("SCAN CONTROL PANEL");
            }
            XmlDataAdaptor pv_logger_id = params_scan2D.childAdaptor("pv_logger_id");
            if (pv_logger_id != null && pv_logger_id.intValue("Id") > 0) {
                snapshotId = pv_logger_id.intValue("Id");
                snapshotIdLabel.setText(snapshotIdString + snapshotId + "  ");
                pvLogged = true;
            } else {
                snapshotId = -1;
                pvLogged = false;
                snapshotIdLabel.setText(noSnapshotIdString);
            }
            XmlDataAdaptor params_scan_panel_paramRB_label = params_scan2D.childAdaptor("sc_panel_paramRB_label");
            if (params_scan_panel_paramRB_label != null) {
                scanController.getParamRB_Label().setText(params_scan_panel_paramRB_label.stringValue("label"));
            }
            XmlDataAdaptor params_scan_panel_paramStep_label = params_scan2D.childAdaptor("sc_panel_paramStep_label");
            if (params_scan_panel_paramStep_label != null) {
                scanController.getParamScanStep_Label().setText(params_scan_panel_paramStep_label.stringValue("label"));
            }
            XmlDataAdaptor params_scan_panel_scanRB_label = params_scan2D.childAdaptor("sc_panel_scanRB_label");
            if (params_scan_panel_scanRB_label != null) {
                scanController.getValueRB_Label().setText(params_scan_panel_scanRB_label.stringValue("label"));
            }
            XmlDataAdaptor params_scan_panel_scanStep_label = params_scan2D.childAdaptor("sc_panel_scanStep_label");
            if (params_scan_panel_scanStep_label != null) {
                scanController.getScanStep_Label().setText(params_scan_panel_scanStep_label.stringValue("label"));
            }
            XmlDataAdaptor params_scan_panel_paramUnits_label = params_scan2D.childAdaptor("sc_panel_paramUnits_label");
            if (params_scan_panel_paramUnits_label != null) {
                scanController.getParamUnitsLabel().setText(params_scan_panel_paramUnits_label.stringValue("label"));
            }
            XmlDataAdaptor params_scan_panel_scanUnits_label = params_scan2D.childAdaptor("sc_panel_scanUnits_label");
            if (params_scan_panel_scanUnits_label != null) {
                scanController.getUnitsLabel().setText(params_scan_panel_scanUnits_label.stringValue("label"));
            }
            XmlDataAdaptor paramPV_tree_node_name = params_scan2D.childAdaptor("parameterPV_tree_name");
            if (paramPV_tree_node_name != null && paramPV_tree_node_name.stringValue("name") != null) {
                rootParameterPV_Node.setName(paramPV_tree_node_name.stringValue("name"));
            } else {
                rootParameterPV_Node.setName(rootParameterPV_Node_Name);
            }
            XmlDataAdaptor scanPV_tree_node_name = params_scan2D.childAdaptor("scanPV_tree_name");
            if (scanPV_tree_node_name != null && scanPV_tree_node_name.stringValue("name") != null) {
                rootScanPV_Node.setName(scanPV_tree_node_name.stringValue("name"));
            } else {
                rootScanPV_Node.setName(rootScanPV_Node_Name);
            }
            XmlDataAdaptor measuredPVs_tree_node_name = params_scan2D.childAdaptor("measuredPVs_tree_name");
            if (measuredPVs_tree_node_name != null && measuredPVs_tree_node_name.stringValue("name") != null) {
                measuredPVs_Node.setName(measuredPVs_tree_node_name.stringValue("name"));
            } else {
                measuredPVs_Node.setName(measuredPVs_Node_Name);
            }
            XmlDataAdaptor validationPVs_tree_node_name = params_scan2D.childAdaptor("validationPVs_tree_name");
            if (validationPVs_tree_node_name != null && validationPVs_tree_node_name.stringValue("name") != null) {
                validationPVs_Node.setName(validationPVs_tree_node_name.stringValue("name"));
            } else {
                validationPVs_Node.setName(validationPVs_Node_Name);
            }
            XmlDataAdaptor params_UseTimeStamp = params_scan2D.childAdaptor("UseTimeStamp");
            if (params_UseTimeStamp != null && params_UseTimeStamp.hasAttribute("yes")) {
                useTimeStampButton.setSelected(params_UseTimeStamp.booleanValue("yes"));
            }
            XmlDataAdaptor params_limits = params_scan2D.childAdaptor("limits_step_delay");
            scanController.setLowLimit(params_limits.doubleValue("low"));
            scanController.setUppLimit(params_limits.doubleValue("upp"));
            scanController.setStep(params_limits.doubleValue("step"));
            scanController.setParamLowLimit(params_limits.doubleValue("paramLow"));
            scanController.setParamUppLimit(params_limits.doubleValue("paramUpp"));
            scanController.setParamStep(params_limits.doubleValue("paramStep"));
            scanController.setSleepTime(params_limits.doubleValue("delay"));
            XmlDataAdaptor params_trigger = params_scan2D.childAdaptor("beam_trigger");
            if (params_trigger != null) {
                scanController.setBeamTriggerDelay(params_trigger.doubleValue("delay"));
                scanController.setBeamTriggerState(params_trigger.booleanValue("on"));
            }
            XmlDataAdaptor params_averg = params_scan2D.childAdaptor("averaging");
            avgCntr.setOnOff(params_averg.booleanValue("on"));
            avgCntr.setAvgNumber(params_averg.intValue("N"));
            avgCntr.setTimeDelay(params_averg.doubleValue("delay"));
            XmlDataAdaptor params_validation = params_scan2D.childAdaptor("validation");
            vldCntr.setOnOff(params_validation.booleanValue("on"));
            vldCntr.setLowLim(params_validation.doubleValue("low"));
            vldCntr.setUppLim(params_validation.doubleValue("upp"));
            XmlDataAdaptor params_paramPV_name = paramPV_scan2D.childAdaptor("PV");
            if (params_paramPV_name != null) {
                String PV_name = params_paramPV_name.stringValue("name");
                PVTreeNode pvNodeNew = new PVTreeNode(PV_name);
                Channel channel = ChannelFactory.defaultFactory().getChannel(PV_name);
                parameterPV_Controller.setChannel(channel);
                pvNodeNew.setChannel(channel);
                pvNodeNew.setAsPVName(true);
                pvNodeNew.setCheckBoxVisible(parameterPV_Node.isCheckBoxVisible());
                parameterPV_Node.add(pvNodeNew);
                pvNodeNew.setSwitchedOnOffListener(parameterPV_Node.getSwitchedOnOffListener());
                pvNodeNew.setCreateRemoveListener(parameterPV_Node.getCreateRemoveListener());
                pvNodeNew.setRenameListener(parameterPV_Node.getRenameListener());
            }
            XmlDataAdaptor params_paramPV_nameRB = paramPV_scan2D.childAdaptor("PV_RB");
            if (params_paramPV_nameRB != null) {
                String PV_nameRB = params_paramPV_nameRB.stringValue("name");
                PVTreeNode pvNodeNew = new PVTreeNode(PV_nameRB);
                Channel channel = ChannelFactory.defaultFactory().getChannel(PV_nameRB);
                parameterPV_Controller.setChannelRB(channel);
                pvNodeNew.setChannel(channel);
                pvNodeNew.setAsPVName(true);
                pvNodeNew.setCheckBoxVisible(parameterPV_RB_Node.isCheckBoxVisible());
                parameterPV_RB_Node.add(pvNodeNew);
                pvNodeNew.setSwitchedOnOffListener(parameterPV_RB_Node.getSwitchedOnOffListener());
                pvNodeNew.setCreateRemoveListener(parameterPV_RB_Node.getCreateRemoveListener());
                pvNodeNew.setRenameListener(parameterPV_RB_Node.getRenameListener());
            }
            String paramPV_PanelTitle = paramPV_scan2D.stringValue("panel_title");
            if (paramPV_PanelTitle != null) {
                parameterPV_Controller.setTitle(paramPV_PanelTitle);
            } else {
                parameterPV_Controller.setTitle("PARAMETER PV CONTROL");
            }
            XmlDataAdaptor scan_PV_name_DA = scanPV_scan2D.childAdaptor("PV");
            if (scan_PV_name_DA != null) {
                String scan_PV_name = scan_PV_name_DA.stringValue("name");
                boolean scan_PV_on = scan_PV_name_DA.booleanValue("on");
                scanPV_ShowState = scan_PV_on;
                PVTreeNode pvNodeNew = new PVTreeNode(scan_PV_name);
                Channel channel = ChannelFactory.defaultFactory().getChannel(scan_PV_name);
                pvNodeNew.setChannel(channel);
                pvNodeNew.setAsPVName(true);
                pvNodeNew.setCheckBoxVisible(scanPV_Node.isCheckBoxVisible());
                scanPV_Node.add(pvNodeNew);
                pvNodeNew.setSwitchedOnOffListener(scanPV_Node.getSwitchedOnOffListener());
                pvNodeNew.setCreateRemoveListener(scanPV_Node.getCreateRemoveListener());
                pvNodeNew.setRenameListener(scanPV_Node.getRenameListener());
                scanVariable.setChannel(channel);
                graphScan.setAxisNames("Scan PV : " + scan_PV_name, "Measured Values");
                graphAnalysis.setAxisNames("Scan PV : " + scan_PV_name, "Measured Values");
                pvNodeNew.setSwitchedOn(scanPV_ShowState);
            }
            XmlDataAdaptor scan_PV_RB_name_DA = scanPV_scan2D.childAdaptor("PV_RB");
            if (scan_PV_RB_name_DA != null) {
                String scan_PV_RB_name = scan_PV_RB_name_DA.stringValue("name");
                boolean scan_PV_RB_on = scan_PV_RB_name_DA.booleanValue("on");
                scanPV_RB_ShowState = scan_PV_RB_on;
                PVTreeNode pvNodeNew = new PVTreeNode(scan_PV_RB_name);
                Channel channel = ChannelFactory.defaultFactory().getChannel(scan_PV_RB_name);
                pvNodeNew.setChannel(channel);
                pvNodeNew.setAsPVName(true);
                pvNodeNew.setCheckBoxVisible(scanPV_RB_Node.isCheckBoxVisible());
                scanPV_RB_Node.add(pvNodeNew);
                pvNodeNew.setSwitchedOnOffListener(scanPV_RB_Node.getSwitchedOnOffListener());
                pvNodeNew.setCreateRemoveListener(scanPV_RB_Node.getCreateRemoveListener());
                pvNodeNew.setRenameListener(scanPV_RB_Node.getRenameListener());
                scanVariable.setChannelRB(channel);
                pvNodeNew.setSwitchedOn(scanPV_RB_ShowState);
            }
            Iterator<XmlDataAdaptor> validation_children = validationPVs_scan2D.childAdaptorIterator();
            while (validation_children.hasNext()) {
                XmlDataAdaptor validationPV_DA = validation_children.next();
                String name = validationPV_DA.stringValue("name");
                boolean onOff = validationPV_DA.booleanValue("on");
                PVTreeNode pvNodeNew = new PVTreeNode(name);
                Channel channel = ChannelFactory.defaultFactory().getChannel(name);
                pvNodeNew.setChannel(channel);
                pvNodeNew.setAsPVName(true);
                pvNodeNew.setCheckBoxVisible(validationPVs_Node.isCheckBoxVisible());
                validationPVs_Node.add(pvNodeNew);
                pvNodeNew.setSwitchedOn(onOff);
                pvNodeNew.setSwitchedOnOffListener(validationPVs_Node.getSwitchedOnOffListener());
                pvNodeNew.setCreateRemoveListener(validationPVs_Node.getCreateRemoveListener());
                pvNodeNew.setRenameListener(validationPVs_Node.getRenameListener());
                MeasuredValue mv_tmp = new MeasuredValue("validation_pv_" + monitoredPV_Count);
                monitoredPV_Count++;
                mv_tmp.setChannel(pvNodeNew.getChannel());
                validationValuesV.add(mv_tmp);
                if (onOff) {
                    scanController.addValidationValue(mv_tmp);
                }
            }
            java.util.Iterator<XmlDataAdaptor> measuredPVs_children = measurePVs_scan2D.childAdaptorIterator();
            while (measuredPVs_children.hasNext()) {
                XmlDataAdaptor measuredPV_DA = measuredPVs_children.next();
                String name = measuredPV_DA.stringValue("name");
                boolean onOff = measuredPV_DA.booleanValue("on");
                boolean unWrappedData = false;
                if (measuredPV_DA.stringValue("unWrapped") != null) {
                    unWrappedData = measuredPV_DA.booleanValue("unWrapped");
                }
                PVTreeNode pvNodeNew = new PVTreeNode(name);
                Channel channel = ChannelFactory.defaultFactory().getChannel(name);
                pvNodeNew.setChannel(channel);
                pvNodeNew.setAsPVName(true);
                pvNodeNew.setCheckBoxVisible(measuredPVs_Node.isCheckBoxVisible());
                measuredPVs_Node.add(pvNodeNew);
                pvNodeNew.setSwitchedOn(onOff);
                pvNodeNew.setSwitchedOnOffListener(measuredPVs_Node.getSwitchedOnOffListener());
                pvNodeNew.setCreateRemoveListener(measuredPVs_Node.getCreateRemoveListener());
                pvNodeNew.setRenameListener(measuredPVs_Node.getRenameListener());
                MeasuredValue mv_tmp = new MeasuredValue("measured_pv_" + monitoredPV_Count);
                mv_tmp.generateUnwrappedData(unWrappedData);
                monitoredPV_Count++;
                mv_tmp.setChannel(pvNodeNew.getChannel());
                measuredValuesShowStateV.add(new Boolean(onOff));
                measuredValuesV.add(mv_tmp);
                if (onOff) {
                    scanController.addMeasuredValue(mv_tmp);
                }
                java.util.Iterator<XmlDataAdaptor> dataIt = measuredPV_DA.childAdaptorIterator("Graph_For_scanPV");
                while (dataIt.hasNext()) {
                    BasicGraphData gd = new BasicGraphData();
                    mv_tmp.addNewDataConatainer(gd);
                    XmlDataAdaptor data = dataIt.next();
                    String legend = data.stringValue("legend");
                    XmlDataAdaptor paramDataValue = data.childAdaptor("parameter_value");
                    if (paramDataValue != null) {
                        double parameter_value = paramDataValue.doubleValue("value");
                        gd.setGraphProperty("PARAMETER_VALUE", new Double(parameter_value));
                    }
                    XmlDataAdaptor paramDataValueRB = data.childAdaptor("parameter_value_RB");
                    if (paramDataValueRB != null) {
                        double parameter_value_RB = paramDataValueRB.doubleValue("value");
                        gd.setGraphProperty("PARAMETER_VALUE_RB", new Double(parameter_value_RB));
                    }
                    gd.setGraphProperty(graphScan.getLegendKeyString(), legend);
                    java.util.Iterator<XmlDataAdaptor> xyerrIt = data.childAdaptorIterator("XYErr");
                    while (xyerrIt.hasNext()) {
                        XmlDataAdaptor xyerr = xyerrIt.next();
                        gd.addPoint(xyerr.doubleValue("x"), xyerr.doubleValue("y"), xyerr.doubleValue("err"));
                    }
                }
                dataIt = measuredPV_DA.childAdaptorIterator("Graph_For_scanPV_RB");
                while (dataIt.hasNext()) {
                    XmlDataAdaptor data = dataIt.next();
                    String legend = data.stringValue("legend");
                    BasicGraphData gd = new BasicGraphData();
                    mv_tmp.addNewDataConatainerRB(gd);
                    if (gd != null) {
                        gd.setGraphProperty(graphScan.getLegendKeyString(), legend);
                        java.util.Iterator<XmlDataAdaptor> xyerrIt = data.childAdaptorIterator("XYErr");
                        while (xyerrIt.hasNext()) {
                            XmlDataAdaptor xyerr = xyerrIt.next();
                            gd.addPoint(xyerr.doubleValue("x"), xyerr.doubleValue("y"), xyerr.doubleValue("err"));
                        }
                    }
                }
            }
            analysisController.createChildAnalysis(analysisConfig);
            setColors(-1);
            updateDataSetOnGraphPanels();
        }
    }
}
