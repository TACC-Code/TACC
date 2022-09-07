class BackupThread extends Thread {
    public Object call() {
        IntelligentFileReader lr = null;
        PrintWriter out = null;
        HttpURLConnection connection = null;
        if (wkn == null) {
            JTable jTable = new JTable(TickerSymbolUtil.getTableModel());
            jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jTable.setRowSorter(new TableRowSorter<TableModel>(jTable.getModel()));
            JDialog dialog = new JDialog((JFrame) null, "Select stock symbol", true);
            Container contentPane = dialog.getContentPane();
            contentPane.setLayout(new BorderLayout());
            contentPane.add(new JScrollPane(jTable), BorderLayout.CENTER);
            dialog.setSize(new Dimension(600, 400));
            dialog.setResizable(false);
            if (JDialog.isDefaultLookAndFeelDecorated()) {
                boolean supportsWindowDecorations = UIManager.getLookAndFeel().getSupportsWindowDecorations();
                if (supportsWindowDecorations) {
                    dialog.setUndecorated(true);
                }
            }
            WindowAdapter adapter = new WindowAdapter() {

                private boolean gotFocus = false;

                public void windowClosing(WindowEvent we) {
                }

                public void windowGainedFocus(WindowEvent we) {
                    if (!gotFocus) {
                        gotFocus = true;
                    }
                }
            };
            dialog.addWindowListener(adapter);
            dialog.addWindowFocusListener(adapter);
            dialog.setVisible(true);
            dialog.dispose();
            wkn = (String) jTable.getRowSorter().getModel().getValueAt(jTable.getSelectedRow(), 0);
        }
        if (wkn == null) return null;
        wkn = wkn.toUpperCase();
        setStatus("Loading " + wkn + "...");
        try {
            setProgress(0);
            List<Matrix> mList = new ArrayList<Matrix>();
            getVariable().setSize(1, 1);
            int index = 1;
            int progress = 0;
            getVariable().setLabel(wkn);
            Calendar today = Calendar.getInstance();
            String startMonth = "00";
            String endMonth = "11";
            String startDay = "1";
            String endDay = "31";
            String update = "d";
            int countPerPage = 365;
            for (int year = today.get(Calendar.YEAR); year >= 1900; year--) {
                String urlString = "http://ichart.yahoo.com/table.csv";
                urlString += "?s=" + wkn;
                urlString += "&a=" + startMonth;
                urlString += "&b=" + startDay;
                urlString += "&c=" + year;
                urlString += "&d=" + endMonth;
                urlString += "&e=" + endDay;
                urlString += "&f=" + year;
                urlString += "&g=" + update;
                urlString += "&z=" + countPerPage;
                urlString += "&ignore=.csv";
                URL url = new URL(urlString);
                File dir = null;
                if (!dir.exists()) dir.mkdirs();
                File file = new File(dir.getAbsolutePath() + File.separator + wkn + "-" + year + ".csv");
                String[] fields = null;
                if (file.exists() && file.length() == 0) {
                    file.delete();
                }
                if (file.exists()) {
                    System.out.println("get from file: " + file);
                    lr = new IntelligentFileReader(file);
                } else {
                    try {
                        System.out.println("get from url: " + urlString);
                        if (year < today.get(Calendar.YEAR)) out = new PrintWriter(new FileOutputStream(file, false));
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.setDoOutput(true);
                        connection.setRequestMethod("GET");
                        connection.connect();
                        lr = new IntelligentFileReader(connection);
                    } catch (FileNotFoundException e) {
                        if (out != null) {
                            out.println("Date,Open,High,Low,Close,Volume,Adj Close");
                        }
                    } catch (Exception e) {
                        logger.log(Level.WARNING, "could not read url " + url, e);
                        try {
                            file.delete();
                        } catch (Exception ex) {
                        }
                    }
                }
                String line = null;
                while (lr != null && (line = lr.readLine()) != null) {
                    if (out != null) out.println(line);
                    if (!line.startsWith("Date")) {
                        fields = line.split(",");
                        String date = fields[0];
                        String open = fields[1];
                        String high = fields[2];
                        String low = fields[3];
                        String close = fields[4];
                        String volume = fields[5];
                        String adjclose = fields[6];
                        Matrix m = MatrixFactory.zeros(1, 1);
                        m.setLabel(date);
                        m.setDouble(Double.parseDouble(close), 0, 0);
                        mList.add(m);
                        progress++;
                    }
                }
                try {
                    if (lr != null) {
                        lr.close();
                        lr = null;
                    }
                } catch (Exception e) {
                }
                try {
                    if (connection != null) {
                        connection.disconnect();
                        connection = null;
                    }
                } catch (Exception e) {
                }
                try {
                    if (out != null) {
                        out.close();
                        out = null;
                    }
                } catch (Exception e) {
                }
            }
            boolean convertToPercent = true;
            if (mList.size() == 0) {
                logger.log(Level.WARNING, "no data loaded for WKN " + wkn);
            } else {
                if (convertToPercent) {
                    getVariable().setMemorySize(mList.size() - 1);
                    Matrix ref = mList.get(mList.size() - 1);
                    for (int i = mList.size() - 2; i >= 0; i--) {
                        Matrix comp = mList.get(i);
                        double r = ref.getDouble(0, 0);
                        double c = comp.getDouble(0, 0);
                        Matrix m = MatrixFactory.zeros(1, 1);
                        m.setDouble((c - r) / r * 50, 0, 0);
                        m.setLabel(ref.getLabel() + " -> " + comp.getLabel());
                        getVariable().addMatrix(m);
                        ref = comp;
                    }
                } else {
                    getVariable().setMemorySize(mList.size());
                    for (int i = mList.size() - 1; i >= 0; i--) {
                        Matrix m = mList.get(i);
                        getVariable().addMatrix(m);
                    }
                }
            }
            setStatus("Chart loaded.");
            return null;
        } catch (Exception e) {
            logger.log(Level.WARNING, "could not load chart", e);
            setStatus("Could not load chart");
            return null;
        } finally {
            setProgress(1);
            try {
                if (lr != null) lr.close();
            } catch (Exception e) {
            }
            try {
                if (connection != null) connection.disconnect();
            } catch (Exception e) {
            }
            try {
                if (out != null) out.close();
            } catch (Exception e) {
            }
        }
    }
}
