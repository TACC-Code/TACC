class BackupThread extends Thread {
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
}
