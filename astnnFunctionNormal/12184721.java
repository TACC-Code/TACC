class BackupThread extends Thread {
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
}
