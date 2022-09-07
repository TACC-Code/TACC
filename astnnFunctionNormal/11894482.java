class BackupThread extends Thread {
    public void actionPerformed(ActionEvent ev) {
        if (ev.getActionCommand().equals("configuration")) {
            configDialog.setVisible(true);
        } else if (ev.getActionCommand().equals("paramsSet")) {
            maxTime = Math.round((int) df6.getValue());
            len = Math.round((int) df7.getValue());
            configDialog.setVisible(false);
        } else if (ev.getActionCommand().equals("cancelConf")) {
            configDialog.setVisible(false);
        } else if (ev.getActionCommand().equals("findQuadError")) {
            progBar.setValue(0);
            if (!isOnline) {
                PVLoggerDataSource plds = new PVLoggerDataSource(defPVLogId);
                HashMap<String, Double> quadMap = plds.getQuadPSMap();
                qSetVals = new double[qPSs.size()];
                for (int i = 0; i < qPSs.size(); i++) {
                    qSetVals[i] = quadMap.get(qPSs.get(i).getChannel(MagnetMainSupply.FIELD_SET_HANDLE).getId());
                    quadTableModel.setValueAt(numberFormat.format(qSetVals[i]), i, 1);
                }
                cqs = new CalcQuadSettings((Ring) myDoc.getSelectedSequence(), goodBPMs, this);
                Thread thread = new Thread(cqs);
                thread.start();
            } else {
                Thread thread = new Thread(cqs);
                thread.start();
            }
        } else if (ev.getActionCommand().equals("setQuads")) {
            try {
                for (int i = 0; i < qPSs.size(); i++) {
                    qPSs.get(i).setField(numberFormat.parse((String) quadTableModel.getValueAt(i, 4)).doubleValue());
                }
            } catch (PutException pe) {
                System.out.println(pe);
            } catch (ConnectionException ce) {
                System.out.println(ce);
            } catch (ParseException pe) {
                System.out.println(pe);
            }
        } else if (ev.getActionCommand().equals("dumpData")) {
            String currentDirectory = _datFileTracker.getRecentFolderPath();
            JFileChooser fileChooser = new JFileChooser(currentDirectory);
            int status = fileChooser.showSaveDialog(this);
            if (status == JFileChooser.APPROVE_OPTION) {
                _datFileTracker.cacheURL(fileChooser.getSelectedFile());
                File file = fileChooser.getSelectedFile();
                try {
                    FileWriter fileWriter = new FileWriter(file);
                    NumberFormat nf = NumberFormat.getNumberInstance();
                    nf.setMaximumFractionDigits(5);
                    nf.setMinimumFractionDigits(5);
                    fileWriter.write("BPM_Id\t\t\t" + "s\t\t" + "xTune\t" + "xPhase\t" + "yTune\t" + "yPhase" + "\n");
                    for (int i = 0; i < xTune.length; i++) {
                        fileWriter.write((allBPMs.get(i)).getId() + "\t" + numberFormat.format(myDoc.getSelectedSequence().getPosition(allBPMs.get(i))) + "\t" + numberFormat.format(xTune[i]) + "\t" + numberFormat.format(xPhase[i]) + "\t" + numberFormat.format(yTune[i]) + "\t" + numberFormat.format(yPhase[i]) + "\n");
                    }
                    String comments = startTime.toString();
                    comments = comments + "\n" + "For Ring Measurement Application";
                    snapshot.setComment(comments);
                    snapshot1.setComment(comments);
                    loggerSession.publishSnapshot(snapshot);
                    loggerSession1.publishSnapshot(snapshot1);
                    pvLoggerId = snapshot.getId();
                    pvLoggerId1 = snapshot1.getId();
                    fileWriter.write("PVLoggerID = " + pvLoggerId + "\tPVLoggerId = " + pvLoggerId1 + "\n");
                    fileWriter.close();
                } catch (IOException ie) {
                    JFrame frame = new JFrame();
                    JOptionPane.showMessageDialog(frame, "Cannot open the file" + file.getName() + "for writing", "Warning!", JOptionPane.PLAIN_MESSAGE);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                }
            }
        }
    }
}
