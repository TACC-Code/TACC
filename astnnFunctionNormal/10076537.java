class BackupThread extends Thread {
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
}
