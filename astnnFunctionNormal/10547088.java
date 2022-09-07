class BackupThread extends Thread {
    private void doAddNewTrain() {
        AddTrainWizard wizard = new AddTrainWizard(this.chartView);
        if (wizard.doWizard() == Wizard.FINISHED) {
            Train train = wizard.getTrain();
            if (train == null) return;
            Chart chart = chartView.mainFrame.chart;
            if (chart.containTrain(train)) {
                if (new YesNoBox(chartView.mainFrame, String.format(_("Train %s is already in the graph, overwrite?"), train.getTrainName())).askForYes()) {
                    chartView.mainFrame.chart.delTrain(train);
                    chartView.addTrain(train);
                }
            } else {
                chartView.addTrain(train);
            }
        }
    }
}
