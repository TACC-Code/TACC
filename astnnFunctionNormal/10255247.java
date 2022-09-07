class BackupThread extends Thread {
    private void createReadButton(Composite composite) {
        Button writeButton = new Button(composite, SWT.NONE);
        writeButton.setText("Read from queue");
        writeButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                MQQueueManager qm = null;
                MQQueue queue = null;
                try {
                    qm = new MQQueueManager(qmText.getText());
                    queue = qm.accessQueue(queueText.getText(), MQC.MQOO_INPUT_SHARED);
                    com.ibm.mq.MQMessage mqMessage = new com.ibm.mq.MQMessage();
                    queue.get(mqMessage);
                    MQMD mqmd = new MQMD(mqMessage);
                    mqmdPage.setMQMD(mqmd);
                    BinaryMessageData messageData = new BinaryMessageData(mqMessage);
                    dataPage.setMessageData(messageData);
                    writeToConsole("Message read from queue (lenght: " + mqMessage.getTotalMessageLength() + ")");
                } catch (MQException ex) {
                    writeToConsole(ex.getMessage());
                } catch (IOException ex) {
                    writeToConsole(ex.getMessage());
                } finally {
                    closeQuitely(queue);
                    closeQuitely(qm);
                }
            }
        });
    }
}
