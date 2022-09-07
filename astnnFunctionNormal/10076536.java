class BackupThread extends Thread {
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
}
