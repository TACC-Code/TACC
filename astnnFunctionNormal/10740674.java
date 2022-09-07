class BackupThread extends Thread {
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
}
