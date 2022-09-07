class BackupThread extends Thread {
    private void computeProbabilitiesAndFillingsForLDDevices(final Population p, final double loadfactor) {
        final Population pop = p;
        for (int s = 0; s < this.getInputParameters().getStations().size(); s++) {
            final AnalyticQNStation station = (AnalyticQNStation) this.getInputParameters().getStations().get(s);
            if (station.isLD()) {
                double fillingOfTheStation = 0;
                double sumOverAllJ = 0;
                for (int j = 1; j <= pop.getCompleteNumberOfJobs(); j++) {
                    final double alphaIOfJ = station.getServiceRateMultiplier(j);
                    double sumOfTerm = 0;
                    for (int c = 0; c < this.getInputParameters().getCustomerClasses().size(); c++) {
                        final Class customerClass = (Class) this.getInputParameters().getCustomerClasses().get(c);
                        final double numberInClass = pop.getNumberPerClass().read(customerClass.getClassID());
                        if (numberInClass > 0) {
                            final double throughput = pop.getThroughputPerClass().read(customerClass.getClassID());
                            double serviceDemand = 0.0;
                            if (station.getStationID().startsWith("station")) {
                                serviceDemand = this.getInputParameters().getServiceDemand(station.getStationID(), customerClass.getClassID()) / loadfactor;
                            } else {
                                serviceDemand = this.getInputParameters().getServiceDemand(station.getStationID(), customerClass.getClassID());
                            }
                            final int predecessor = (int) pop.getPredecessor().read(customerClass.getClassID());
                            final Population populationWithOneJobOfClassLess = (Population) this.allPopulations.get(predecessor);
                            final double probabilityOfJMinusAtPredecessorPopulation = populationWithOneJobOfClassLess.getProbabilityPerStation().read(station.getStationID(), j - 1);
                            sumOfTerm = sumOfTerm + ((throughput * serviceDemand) / alphaIOfJ) * probabilityOfJMinusAtPredecessorPopulation;
                            pop.getFillingPerStationAndClass().write(station.getStationID(), customerClass.getClassID(), pop.getFillingPerStationAndClass().read(station.getStationID(), customerClass.getClassID()) + j * sumOfTerm);
                        }
                    }
                    pop.getProbabilityPerStation().write(station.getStationID(), j, sumOfTerm);
                    fillingOfTheStation = fillingOfTheStation + (j * sumOfTerm);
                    sumOverAllJ = sumOverAllJ + sumOfTerm;
                }
                pop.getProbabilityPerStation().write(station.getStationID(), 0, 1 - sumOverAllJ);
                pop.getFillingPerStation().write(station.getStationID(), fillingOfTheStation);
            }
        }
    }
}
