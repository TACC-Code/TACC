class BackupThread extends Thread {
    private boolean writeFileHelper(String path, boolean autoSave) {
        try {
            String htmlSource = Log.getText();
            String htmlSource2 = "";
            if (htmlSource.indexOf("<body>") != -1) {
                int start = htmlSource.indexOf("<body>") + 6;
                htmlSource = htmlSource.substring(start);
                while (htmlSource.contains("<body>")) {
                    start = htmlSource.indexOf("<body>") + 6;
                    htmlSource = htmlSource.substring(start);
                }
                int end = htmlSource.indexOf("</body>");
                if (end == -1) end = htmlSource.length();
                htmlSource = htmlSource.substring(0, end);
            }
            String[] lines = htmlSource.split("\n");
            for (int i = 0; i < lines.length; ++i) {
                htmlSource2 += lines[i];
            }
            htmlSource = "\n<html>\n<head>\n</head>\n<body>\n" + htmlSource2 + "\n</body>\n</html>\n";
            FileOutputStream fos = new FileOutputStream(path);
            GZIPOutputStream gz = new GZIPOutputStream(fos);
            ObjectOutputStream oos = new ObjectOutputStream(gz);
            oos.writeObject(htmlSource2);
            if (!autoSave) Log.setUnchangedStatus();
            int count = DatasheetPane.getTabCount();
            oos.writeObject(new Integer(count));
            for (int i = 0; i < count; ++i) {
                Spreadsheet s = ((SpreadsheetScrollPane) DatasheetPane.getComponentAt(i)).getSpreadsheet();
                if (!autoSave) {
                    s.setUnchangedStatus();
                    s.closeFile();
                }
                oos.writeObject(((SpreadsheetModel) s.getModel()).getTabDelimitedValues());
            }
            if (!autoSave) DatasheetPane.resetTabTitles();
            count = app.getChartFrames().size();
            if (count != 0) {
                oos.writeObject(new Integer(count));
                for (int i = 0; i < count; ++i) {
                    String title = app.getChartFrames().get(i).getChartTitle();
                    JFreeChart chart = app.getChartFrames().get(i).getChart();
                    try {
                        oos.writeObject(title);
                        oos.writeObject(chart);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }
            oos.flush();
            oos.close();
            gz.close();
            fos.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
