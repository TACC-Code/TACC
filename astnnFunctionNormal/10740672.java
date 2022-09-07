class BackupThread extends Thread {
        protected ParameterPV_Controller(ScanVariable scanVariableParameter_In) {
            scanVariableParameter = scanVariableParameter_In;
            paramPV_RB_ValueText.setEditable(false);
            paramPV_ValueText.setDecimalFormat(valueFormat);
            paramPV_RB_ValueText.setDecimalFormat(valueFormat);
            paramPV_ValueText.setHorizontalAlignment(JTextField.CENTER);
            paramPV_RB_ValueText.setHorizontalAlignment(JTextField.CENTER);
            paramPV_ValueText.removeInnerFocusListener();
            paramPV_RB_ValueText.removeInnerFocusListener();
            Border etchedBorder = BorderFactory.createEtchedBorder();
            border = BorderFactory.createTitledBorder(etchedBorder, "PARAMETER PV CONTROL");
            paramPV_Panel.setBorder(border);
            paramPV_Panel.setBackground(paramPV_Panel.getBackground().darker());
            paramPV_ValueText.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    scanVariableParameter.setValue(paramPV_ValueText.getValue());
                }
            });
            readButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (scanVariableParameter.getChannel() != null) {
                        paramPV_ValueText.setValue(scanVariableParameter.getValue());
                    } else {
                        paramPV_ValueText.setText(null);
                        paramPV_ValueText.setBackground(Color.white);
                    }
                    if (scanVariableParameter.getChannelRB() != null) {
                        paramPV_RB_ValueText.setValue(scanVariableParameter.getValueRB());
                    } else {
                        paramPV_RB_ValueText.setText(null);
                        paramPV_RB_ValueText.setBackground(Color.white);
                    }
                }
            });
            JPanel tmp_1 = new JPanel();
            tmp_1.setLayout(new GridLayout(2, 2, 1, 1));
            tmp_1.add(paramPV_Label);
            tmp_1.add(paramPV_ValueText);
            tmp_1.add(paramPV_RB_Label);
            tmp_1.add(paramPV_RB_ValueText);
            JPanel tmp_2 = new JPanel();
            tmp_2.setLayout(new BorderLayout());
            tmp_2.add(tmp_1, BorderLayout.CENTER);
            tmp_2.add(readButton, BorderLayout.SOUTH);
            paramPV_Panel.setLayout(new BorderLayout());
            paramPV_Panel.add(tmp_2, BorderLayout.NORTH);
        }
}
