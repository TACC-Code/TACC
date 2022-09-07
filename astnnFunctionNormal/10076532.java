class BackupThread extends Thread {
    private void makeIntersectionFindingPanel() {
        markerPos_Text.setEditable(true);
        pvSetVal_Text.setEditable(false);
        pvRBVal_Text.setEditable(false);
        markerPos_Text.setDecimalFormat(val_Format);
        pvSetVal_Text.setDecimalFormat(val_Format);
        pvRBVal_Text.setDecimalFormat(val_Format);
        markerPos_Text.setHorizontalAlignment(JTextField.CENTER);
        pvSetVal_Text.setHorizontalAlignment(JTextField.CENTER);
        pvRBVal_Text.setHorizontalAlignment(JTextField.CENTER);
        markerPos_Text.removeInnerFocusListener();
        pvSetVal_Text.removeInnerFocusListener();
        pvRBVal_Text.removeInnerFocusListener();
        findIntersectionPanel.setLayout(new BorderLayout());
        Border etchedBorder = BorderFactory.createEtchedBorder();
        findIntersectionPanel.setBorder(etchedBorder);
        JPanel temp_0 = new JPanel();
        temp_0.setLayout(new GridLayout(1, 2, 1, 1));
        temp_0.add(markerPos_Label);
        temp_0.add(markerPos_Text);
        JPanel temp_1 = new JPanel();
        temp_1.setLayout(new BorderLayout());
        temp_1.add(find_Button, BorderLayout.NORTH);
        temp_1.add(temp_0, BorderLayout.CENTER);
        temp_1.add(setVal_Button, BorderLayout.SOUTH);
        JPanel temp_2 = new JPanel();
        temp_2.setLayout(new GridLayout(2, 2, 1, 1));
        temp_2.add(pvSet_Label);
        temp_2.add(pvSetVal_Text);
        temp_2.add(pvRB_Label);
        temp_2.add(pvRBVal_Text);
        JPanel temp_3 = new JPanel();
        temp_3.setLayout(new BorderLayout());
        temp_3.add(temp_1, BorderLayout.NORTH);
        temp_3.add(temp_2, BorderLayout.CENTER);
        temp_3.add(readVal_Button, BorderLayout.SOUTH);
        findIntersectionPanel.add(temp_3, BorderLayout.NORTH);
        dragVerLine_Listener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int ind = graphAnalysis.getDraggedLineIndex();
                double phase = graphAnalysis.getVerticalValue(ind);
                double shift = mainController.getPhaseShift(graphAnalysis.getAllGraphData());
                markerPos = phase - shift;
                if (shift != 0.) {
                    markerPos += 180.;
                    while (markerPos < 0.) {
                        markerPos += 360.;
                    }
                    markerPos = markerPos % 360.;
                    markerPos -= 180.;
                }
                markerPos_Text.setValueQuietly(markerPos);
            }
        };
        markerPos_Text.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                graphAnalysis.addDraggedVerLinesListener(null);
                markerPos = markerPos_Text.getValue();
                double shift = mainController.getPhaseShift(graphAnalysis.getAllGraphData());
                double phase = markerPos + shift;
                if (shift != 0.) {
                    phase += 180.;
                    while (phase < 0.) {
                        phase += 360.;
                    }
                    phase = phase % 360.;
                    phase -= 180.;
                }
                graphAnalysis.setVerticalLineValue(phase, 0);
                graphAnalysis.addDraggedVerLinesListener(dragVerLine_Listener);
            }
        });
        findIntersection_Listener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                double xMin = graphAnalysis.getCurrentMinX();
                double xMax = graphAnalysis.getCurrentMaxX();
                double yMin = graphAnalysis.getCurrentMinY();
                double yMax = graphAnalysis.getCurrentMaxY();
                Vector<BasicGraphData> interpGD_V = graphAnalysis.getAllGraphData();
                interpGD_V.remove(graphDataLocal);
                if (interpGD_V.size() > 1) {
                    Double[] intersectV = GraphDataOperations.findIntersection(interpGD_V, xMin, xMax, yMin, yMax, 0.001);
                    if (intersectV[0] != null) {
                        graphAnalysis.addDraggedVerLinesListener(null);
                        double phase = intersectV[0].doubleValue();
                        double shift = mainController.getPhaseShift(interpGD_V);
                        markerPos = phase - shift;
                        if (shift != 0.) {
                            markerPos += 180.;
                            while (markerPos < 0.) {
                                markerPos += 360.;
                            }
                            markerPos = markerPos % 360.;
                            markerPos -= 180.;
                        }
                        markerPos_Text.setValue(markerPos);
                        graphAnalysis.addDraggedVerLinesListener(dragVerLine_Listener);
                        messageTextLocal.setText(null);
                        messageTextLocal.setText("The intersection point x=" + val_Format.format(intersectV[0].doubleValue()) + " +- " + val_Format.format(intersectV[2].doubleValue()) + "   shift = " + val_Format.format(shift));
                    } else {
                        Toolkit.getDefaultToolkit().beep();
                        messageTextLocal.setText(null);
                        messageTextLocal.setText("Cannot find intersection.");
                    }
                } else {
                    Toolkit.getDefaultToolkit().beep();
                    messageTextLocal.setText(null);
                    messageTextLocal.setText("Cannot find intersection. Do not have enough data.");
                }
            }
        };
        setVal_Button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                double val = markerPos_Text.getValue();
                if (scanVariable.getChannel() != null) {
                    scanVariable.setValue(val);
                } else {
                    messageTextLocal.setText(null);
                    messageTextLocal.setText("The scan PV channel does not exist.");
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        });
        readVal_Button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (scanVariable.getChannel() != null) {
                    pvSetVal_Text.setValue(scanVariable.getValue());
                } else {
                    pvSetVal_Text.setText(null);
                    pvSetVal_Text.setBackground(Color.white);
                }
                if (scanVariable.getChannelRB() != null) {
                    pvRBVal_Text.setValue(scanVariable.getValueRB());
                } else {
                    pvRBVal_Text.setText(null);
                    pvRBVal_Text.setBackground(Color.white);
                }
            }
        });
        find_Button.addActionListener(findIntersection_Listener);
        find_Button.setForeground(Color.blue);
        setVal_Button.setForeground(Color.blue);
        readVal_Button.setForeground(Color.blue);
    }
}
