class BackupThread extends Thread {
    private void doClose(int retStatus) {
        returnStatus = retStatus;
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("returnStatus", retStatus + "");
        try {
            Writer writer = new StringWriter();
            jTextArea1.write(writer);
            properties.put("lines", IOUtils.readLines(IOUtils.toInputStream(writer.toString())));
        } catch (Exception e) {
        }
        if (callback != null) callback.call(properties);
        setVisible(false);
        dispose();
    }
}
