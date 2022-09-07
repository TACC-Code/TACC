class BackupThread extends Thread {
        public void actionPerformed(ActionEvent e) {
            final NotesBuilder builder = new NotesBuilder();
            try {
                MessageRecorder recorder = new MessageRecorder((String) deviceComboBox.getSelectedItem()) {

                    @Override
                    public boolean messageRecorded(MidiMessage message) {
                        byte[] datas = message.getMessage();
                        builder.analyse(datas);
                        return true;
                    }
                };
                int result = messageBox.show(KeyboardPanel.this);
                recorder.close();
                if (result == MessageBox.OPTION_OK && builder.getChannel() != -1) {
                    channelSpinner.setValue(builder.getChannel() + 1);
                    fromSpinner.setValue(builder.getFrom());
                    toSpinner.setValue(builder.getTo());
                }
            } catch (MidiUnavailableException cannotRecord) {
            }
        }
}
