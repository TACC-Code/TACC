class BackupThread extends Thread {
    private void refreshSysInfo() {
        _jmxService.getRuntimeSystemInfo(new AsyncCallback<RuntimeSystemInfo>() {

            public void onSuccess(RuntimeSystemInfo usage) {
                try {
                    _cpuTimeList.add(new long[] { usage.getCpuTime(), usage.getUserTime() });
                    if (_cpuTimeList.size() > NUM_CPU_DISPLAY) {
                        _cpuTimeList.removeFirst();
                    }
                    AbstractDataTable memDataTable = createMemTable(usage);
                    PieChart.Options memOptions = createMemOptions("Memory Usage");
                    AbstractDataTable cpuDataTable = createCPUTable(_cpuTimeList);
                    AreaChart.Options cpuOptions = createCPUOptions("CPU Time");
                    memChart.draw(memDataTable, memOptions);
                    cpuChart.draw(cpuDataTable, cpuOptions);
                } catch (Throwable th) {
                    memChart.draw(createMemTable(null), createMemOptions("Error: " + th.getMessage()));
                }
            }

            public void onFailure(Throwable throwable) {
                memChart.draw(createMemTable(null), createMemOptions("Error: " + throwable.getMessage()));
            }
        });
        _jmxService.getZoieSystemInfo(new AsyncCallback<ZoieServerInfo>() {

            public void onFailure(Throwable arg0) {
                infoTable.setText(0, 0, "ERROR");
            }

            public void onSuccess(ZoieServerInfo zsi) {
                String[] names = zsi.getNames();
                String[] values = zsi.getValues();
                for (int i = 0; i < names.length; i++) {
                    infoTable.setText(i, 0, names[i]);
                    infoTable.setText(i, 1, values[i]);
                }
            }
        });
        _jmxService.getDataProviderInfo(new AsyncCallback<ZoieServerInfo>() {

            public void onFailure(Throwable arg0) {
                dataProviderTable.setText(0, 0, "ERROR");
            }

            public void onSuccess(ZoieServerInfo zsi) {
                String[] names = zsi.getNames();
                String[] values = zsi.getValues();
                boolean[] writeable = zsi.getWritable();
                boolean[] readable = zsi.getReadable();
                int i = 0;
                for (i = 0; i < names.length; i++) {
                    if (readable[i] && (!writeable[i])) {
                        dataProviderTable.setText(i, 0, names[i]);
                        dataProviderTable.setText(i, 1, values[i]);
                    } else if (writeable[i]) {
                        dataProviderTable.setText(i, 0, names[i]);
                        TextBox tb = new TextBox();
                        tb.setText(values[i]);
                        tb.addValueChangeHandler(new ValueChangeHandler<String>() {

                            public void onValueChange(ValueChangeEvent<String> event) {
                                System.out.println(event.getSource().toString());
                            }
                        });
                        dataProviderTable.setWidget(i, 1, tb);
                    }
                }
                Button buttonstart = new Button("start");
                buttonstart.addClickHandler(new ClickHandler() {

                    public void onClick(ClickEvent arg0) {
                        _jmxService.invokeNoParam("start", new AsyncCallback<Void>() {

                            public void onFailure(Throwable arg0) {
                            }

                            public void onSuccess(Void arg0) {
                            }
                        });
                    }
                });
                dataProviderTable.setWidget(i, 1, buttonstart);
                i++;
                Button buttonstop = new Button("stop");
                buttonstop.addClickHandler(new ClickHandler() {

                    public void onClick(ClickEvent arg0) {
                        _jmxService.invokeNoParam("stop", new AsyncCallback<Void>() {

                            public void onFailure(Throwable arg0) {
                            }

                            public void onSuccess(Void arg0) {
                            }
                        });
                    }
                });
                dataProviderTable.setWidget(i, 1, buttonstop);
            }
        });
    }
}
