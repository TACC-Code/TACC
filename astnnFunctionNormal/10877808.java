class BackupThread extends Thread {
    public RunnerController() {
        runnerUC.setUpdateTime(1.0);
        runnerPSG.setName("Runner PS Group");
        runner.addPowerSupplyGroup(runnerPSG);
        JPanel cntrlPanel = new JPanel();
        cntrlPanel.setLayout(new VerticalLayout());
        cntrlPanel.add(runner.getPanel());
        groupList.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "pv goups"));
        cntrlPanel.add(groupList);
        runnerControllerPanel.setLayout(new BorderLayout());
        runnerControllerPanel.add(cntrlPanel, BorderLayout.WEST);
        runnerControllerPanel.add(graphSubPanel, BorderLayout.CENTER);
        SimpleChartPopupMenu.addPopupMenuTo(graphSubPanel);
        graphSubPanel.setOffScreenImageDrawing(true);
        graphSubPanel.setName("Cycler : PV Values vs. Time");
        graphSubPanel.setAxisNames("time, sec", "PV Values");
        graphSubPanel.setGraphBackGroundColor(Color.white);
        graphSubPanel.setLegendButtonVisible(true);
        graphSubPanel.setLegendBackground(Color.white);
        graphSubPanel.setChooseModeButtonVisible(true);
        JScrollPane scrollPane = new JScrollPane(psTable);
        scrollPane.setPreferredSize(new Dimension(1, 150));
        cntrlPanel.add(scrollPane);
        groupList.setVisibleRowCount(5);
        groupList.setEnabled(true);
        groupList.setFixedCellWidth(10);
        ListSelectionListener groupListListener = new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                updatePSCyclerTable();
            }
        };
        groupList.addListSelectionListener(groupListListener);
        AbstractTableModel tableModel = new AbstractTableModel() {

            public String getColumnName(int col) {
                if (col == 0) {
                    return "Power Supply PV";
                }
                return " Use";
            }

            public int getRowCount() {
                return PowerSupplyCyclerV.size();
            }

            public int getColumnCount() {
                return 2;
            }

            public Object getValueAt(int row, int col) {
                PowerSupplyCycler psc = (PowerSupplyCycler) PowerSupplyCyclerV.get(row);
                if (col == 1) {
                    return new Boolean(psc.getActive());
                }
                return psc.getChannelName();
            }

            public boolean isCellEditable(int row, int col) {
                if (col == 1) {
                    return true;
                }
                return false;
            }

            public Class getColumnClass(int c) {
                if (c == 1) {
                    return (new Boolean(true)).getClass();
                }
                return (new String("a")).getClass();
            }

            public void setValueAt(Object value, int row, int col) {
                if (col == 1) {
                    PowerSupplyCycler psc = (PowerSupplyCycler) PowerSupplyCyclerV.get(row);
                    psc.setActive(!psc.getActive());
                    runner.setNeedInit();
                    updateGraphDataSet();
                }
                fireTableCellUpdated(row, col);
            }
        };
        psTable.setModel(tableModel);
        psTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        TableColumn column = null;
        for (int i = 0; i < 2; i++) {
            column = psTable.getColumnModel().getColumn(i);
            if (i == 1) {
                column.setPreferredWidth(1);
            } else {
                column.setPreferredWidth(1000);
            }
        }
        runner.addStartListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                groupList.setEnabled(false);
                psTable.setEnabled(false);
                runnerUC.update();
            }
        });
        runner.addStepListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                runnerUC.update();
            }
        });
        runner.addStopListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (runner.isStartReady()) {
                    groupList.setEnabled(true);
                    psTable.setEnabled(true);
                } else {
                    groupList.setEnabled(false);
                    psTable.setEnabled(false);
                }
                runnerUC.update();
            }
        });
        runner.addInitListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (runner.isStartReady()) {
                    groupList.setEnabled(true);
                    psTable.setEnabled(true);
                } else {
                    groupList.setEnabled(false);
                    psTable.setEnabled(false);
                }
                runnerUC.update();
            }
        });
        runnerUC.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                graphSubPanel.refreshGraphJPanel();
            }
        });
    }
}
