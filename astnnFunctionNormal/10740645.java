class BackupThread extends Thread {
    @Override
    public void saveDocumentAs(URL url) {
        XmlDataAdaptor da = XmlDataAdaptor.newEmptyDocumentAdaptor();
        XmlDataAdaptor scan2D_Adaptor = da.createChild(dataRootName_SR);
        scan2D_Adaptor.setValue("title", url.getFile());
        XmlDataAdaptor params_scan2D = scan2D_Adaptor.createChild(paramsName_SR);
        XmlDataAdaptor paramPV_scan2D = scan2D_Adaptor.createChild(paramPV_SR);
        XmlDataAdaptor scanPV_scan2D = scan2D_Adaptor.createChild(scanPV_SR);
        XmlDataAdaptor validationPVs_scan2D = scan2D_Adaptor.createChild(validationPVs_SR);
        analysisConfig = scan2D_Adaptor.createChild(analysisConfig_SR);
        XmlDataAdaptor measurePVs_scan2D = scan2D_Adaptor.createChild(measurePVs_SR);
        analysisController.dumpChildAnalysisConfig(analysisConfig);
        XmlDataAdaptor params_font = params_scan2D.createChild("font");
        params_font.setValue("name", globalFont.getFamily());
        params_font.setValue("style", globalFont.getStyle());
        params_font.setValue("size", globalFont.getSize());
        XmlDataAdaptor params_scan_panel_title = params_scan2D.createChild("scan_panel_title");
        params_scan_panel_title.setValue("title", scanController.getTitle());
        XmlDataAdaptor pv_logger_id = params_scan2D.createChild("pv_logger_id");
        pv_logger_id.setValue("Id", (int) snapshotId);
        XmlDataAdaptor params_scan_panel_paramRB_label = params_scan2D.createChild("sc_panel_paramRB_label");
        params_scan_panel_paramRB_label.setValue("label", scanController.getParamRB_Label().getText());
        XmlDataAdaptor params_scan_panel_paramStep_label = params_scan2D.createChild("sc_panel_paramStep_label");
        params_scan_panel_paramStep_label.setValue("label", scanController.getParamScanStep_Label().getText());
        XmlDataAdaptor params_scan_panel_scanRB_label = params_scan2D.createChild("sc_panel_scanRB_label");
        params_scan_panel_scanRB_label.setValue("label", scanController.getValueRB_Label().getText());
        XmlDataAdaptor params_scan_panel_scanStep_label = params_scan2D.createChild("sc_panel_scanStep_label");
        params_scan_panel_scanStep_label.setValue("label", scanController.getScanStep_Label().getText());
        XmlDataAdaptor params_scan_panel_paramUnits_label = params_scan2D.createChild("sc_panel_paramUnits_label");
        params_scan_panel_paramUnits_label.setValue("label", scanController.getParamUnitsLabel().getText());
        XmlDataAdaptor params_scan_panel_scanUnits_label = params_scan2D.createChild("sc_panel_scanUnits_label");
        params_scan_panel_scanUnits_label.setValue("label", scanController.getUnitsLabel().getText());
        XmlDataAdaptor paramPV_tree_node_name = params_scan2D.createChild("parameterPV_tree_name");
        paramPV_tree_node_name.setValue("name", rootParameterPV_Node.getName());
        XmlDataAdaptor scanPV_tree_node_name = params_scan2D.createChild("scanPV_tree_name");
        scanPV_tree_node_name.setValue("name", rootScanPV_Node.getName());
        XmlDataAdaptor measuredPVs_tree_node_name = params_scan2D.createChild("measuredPVs_tree_name");
        measuredPVs_tree_node_name.setValue("name", measuredPVs_Node.getName());
        XmlDataAdaptor validationPVs_tree_node_name = params_scan2D.createChild("validationPVs_tree_name");
        validationPVs_tree_node_name.setValue("name", validationPVs_Node.getName());
        XmlDataAdaptor params_UseTimeStamp = params_scan2D.createChild("UseTimeStamp");
        params_UseTimeStamp.setValue("yes", useTimeStampButton.isSelected());
        XmlDataAdaptor params_limits = params_scan2D.createChild("limits_step_delay");
        params_limits.setValue("paramLow", scanController.getParamLowLimit());
        params_limits.setValue("paramUpp", scanController.getParamUppLimit());
        params_limits.setValue("paramStep", scanController.getParamStep());
        params_limits.setValue("low", scanController.getLowLimit());
        params_limits.setValue("upp", scanController.getUppLimit());
        params_limits.setValue("step", scanController.getStep());
        params_limits.setValue("delay", scanController.getSleepTime());
        XmlDataAdaptor params_trigger = params_scan2D.createChild("beam_trigger");
        params_trigger.setValue("on", scanController.getBeamTriggerState());
        params_trigger.setValue("delay", scanController.getBeamTriggerDelay());
        XmlDataAdaptor params_averg = params_scan2D.createChild("averaging");
        params_averg.setValue("on", avgCntr.isOn());
        params_averg.setValue("N", avgCntr.getAvgNumber());
        params_averg.setValue("delay", avgCntr.getTimeDelay());
        XmlDataAdaptor params_validation = params_scan2D.createChild("validation");
        params_validation.setValue("on", vldCntr.isOn());
        params_validation.setValue("low", vldCntr.getInnerLowLim());
        params_validation.setValue("upp", vldCntr.getInnerUppLim());
        paramPV_scan2D.setValue("on", paramPV_ON);
        paramPV_scan2D.setValue("panel_title", parameterPV_Controller.getTitle());
        if (scanVariableParameter.getChannel() != null) {
            XmlDataAdaptor params_paramPV_name = paramPV_scan2D.createChild("PV");
            params_paramPV_name.setValue("name", scanVariableParameter.getChannelName());
        }
        if (scanVariableParameter.getChannelRB() != null) {
            XmlDataAdaptor params_paramPV_nameRB = paramPV_scan2D.createChild("PV_RB");
            params_paramPV_nameRB.setValue("name", scanVariableParameter.getChannelNameRB());
        }
        if (scanVariable.getChannel() != null) {
            XmlDataAdaptor scan_PV_name = scanPV_scan2D.createChild("PV");
            scan_PV_name.setValue("name", scanVariable.getChannelName());
            scan_PV_name.setValue("on", scanPV_ShowState);
        }
        if (scanVariable.getChannelRB() != null) {
            XmlDataAdaptor scan_PV_RB_name = scanPV_scan2D.createChild("PV_RB");
            scan_PV_RB_name.setValue("name", scanVariable.getChannelNameRB());
            scan_PV_RB_name.setValue("on", scanPV_RB_ShowState);
        }
        Enumeration validation_children = validationPVs_Node.children();
        while (validation_children.hasMoreElements()) {
            PVTreeNode pvNode = (PVTreeNode) validation_children.nextElement();
            XmlDataAdaptor validationPV_node = validationPVs_scan2D.createChild("Validation_PV");
            validationPV_node.setValue("name", pvNode.getChannel().channelName());
            validationPV_node.setValue("on", pvNode.isSwitchedOn());
        }
        for (int i = 0, n = measuredValuesV.size(); i < n; i++) {
            MeasuredValue mv_tmp = (MeasuredValue) measuredValuesV.get(i);
            XmlDataAdaptor measuredPV_DA = measurePVs_scan2D.createChild("MeasuredPV");
            measuredPV_DA.setValue("name", mv_tmp.getChannel().channelName());
            measuredPV_DA.setValue("on", ((Boolean) measuredValuesShowStateV.get(i)).booleanValue());
            measuredPV_DA.setValue("unWrapped", new Boolean(mv_tmp.generateUnwrappedDataOn()));
            Vector dataV = mv_tmp.getDataContainers();
            for (int j = 0, nd = dataV.size(); j < nd; j++) {
                BasicGraphData gd = (BasicGraphData) dataV.get(j);
                if (gd.getNumbOfPoints() > 0) {
                    XmlDataAdaptor graph_DA = measuredPV_DA.createChild("Graph_For_scanPV");
                    graph_DA.setValue("legend", (String) gd.getGraphProperty(graphScan.getLegendKeyString()));
                    Double paramValue = (Double) gd.getGraphProperty("PARAMETER_VALUE");
                    if (paramValue != null) {
                        XmlDataAdaptor paramDataValue = graph_DA.createChild("parameter_value");
                        paramDataValue.setValue("value", paramValue.doubleValue());
                    }
                    Double paramValueRB = (Double) gd.getGraphProperty("PARAMETER_VALUE_RB");
                    if (paramValueRB != null) {
                        XmlDataAdaptor paramDataValueRB = graph_DA.createChild("parameter_value_RB");
                        paramDataValueRB.setValue("value", paramValueRB.doubleValue());
                    }
                    for (int k = 0, np = gd.getNumbOfPoints(); k < np; k++) {
                        XmlDataAdaptor point_DA = graph_DA.createChild("XYErr");
                        point_DA.setValue("x", gd.getX(k));
                        point_DA.setValue("y", gd.getY(k));
                        point_DA.setValue("err", gd.getErr(k));
                    }
                }
            }
            dataV = mv_tmp.getDataContainersRB();
            for (int j = 0, nd = dataV.size(); j < nd; j++) {
                BasicGraphData gd = (BasicGraphData) dataV.get(j);
                if (gd.getNumbOfPoints() > 0) {
                    XmlDataAdaptor graph_DA = measuredPV_DA.createChild("Graph_For_scanPV_RB");
                    graph_DA.setValue("legend", (String) gd.getGraphProperty(graphScan.getLegendKeyString()));
                    for (int k = 0, np = gd.getNumbOfPoints(); k < np; k++) {
                        XmlDataAdaptor point_DA = graph_DA.createChild("XYErr");
                        point_DA.setValue("x", gd.getX(k));
                        point_DA.setValue("y", gd.getY(k));
                        point_DA.setValue("err", gd.getErr(k));
                    }
                }
            }
        }
        try {
            scan2D_Adaptor.writeTo(new File(url.getFile()));
            setHasChanges(true);
        } catch (IOException e) {
            System.out.println("IOException e=" + e);
        }
    }
}
