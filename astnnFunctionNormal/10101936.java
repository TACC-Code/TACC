class BackupThread extends Thread {
    public SolutionSet execute() throws JMException {
        int populationSize;
        int maxEvaluations;
        int evaluations;
        QualityIndicator indicators;
        int requiredEvaluations;
        SolutionSet population;
        SolutionSet offspringPopulation;
        SolutionSet union;
        Operator mutationOperator;
        Operator crossoverOperator;
        Operator selectionOperator;
        Distance distance = new Distance();
        populationSize = ((Integer) this.getInputParameter("populationSize")).intValue();
        maxEvaluations = ((Integer) this.getInputParameter("maxEvaluations")).intValue();
        indicators = (QualityIndicator) getInputParameter("indicators");
        population = new SolutionSet(populationSize);
        evaluations = 0;
        mutationOperator = this.operators_.get("mutation");
        crossoverOperator = this.operators_.get("crossover");
        selectionOperator = this.operators_.get("selection");
        requiredEvaluations = 0;
        Solution newSolution;
        for (int i = 0; i < populationSize; i++) {
            newSolution = new Solution(problem_);
            problem_.evaluate(newSolution);
            problem_.evaluateConstraints(newSolution);
            evaluations++;
            population.add(newSolution);
        }
        try {
            FileOutputStream fos = new FileOutputStream("metrics" + ".nsgaIIss");
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            while (evaluations < maxEvaluations) {
                offspringPopulation = new SolutionSet(populationSize);
                Solution[] parents = new Solution[2];
                parents[0] = (Solution) selectionOperator.execute(population);
                parents[1] = (Solution) selectionOperator.execute(population);
                Solution[] offSpring = (Solution[]) crossoverOperator.execute(parents);
                mutationOperator.execute(offSpring[0]);
                problem_.evaluate(offSpring[0]);
                problem_.evaluateConstraints(offSpring[0]);
                offspringPopulation.add(offSpring[0]);
                evaluations++;
                union = ((SolutionSet) population).union(offspringPopulation);
                Ranking ranking = new Ranking(union);
                int remain = populationSize;
                int index = 0;
                SolutionSet front = null;
                population.clear();
                front = ranking.getSubfront(index);
                while ((remain > 0) && (remain >= front.size())) {
                    distance.crowdingDistanceAssignment(front, problem_.getNumberOfObjectives());
                    for (int k = 0; k < front.size(); k++) {
                        population.add(front.get(k));
                    }
                    remain = remain - front.size();
                    index++;
                    if (remain > 0) {
                        front = ranking.getSubfront(index);
                    }
                }
                if (remain > 0) {
                    distance.crowdingDistanceAssignment(front, problem_.getNumberOfObjectives());
                    front.sort(new jmetal.base.operator.comparator.CrowdingComparator());
                    for (int k = 0; k < remain; k++) {
                        population.add(front.get(k));
                    }
                    remain = 0;
                }
                if ((evaluations % 100 == 0) && (indicators != null) && (requiredEvaluations == 0)) {
                    SolutionSet nonDominatedTmp = (new Ranking(population)).getSubfront(0);
                    bw.write(evaluations + "");
                    bw.write(" ");
                    bw.write(indicators.getParetoOptimalSolutions(nonDominatedTmp) + "");
                    bw.write(" ");
                    bw.write(indicators.getGD(nonDominatedTmp) + "");
                    bw.write(" ");
                    bw.write(indicators.getIGD(nonDominatedTmp) + "");
                    bw.write(" ");
                    bw.write(indicators.getEpsilon(nonDominatedTmp) + "");
                    bw.write(" ");
                    bw.write(indicators.getSpread(nonDominatedTmp) + "");
                    bw.write(" ");
                    bw.write(indicators.getHypervolume(nonDominatedTmp) + "");
                    bw.newLine();
                }
            }
            bw.close();
        } catch (IOException e) {
            Configuration.logger_.severe("Error acceding to the file");
            e.printStackTrace();
        }
        setOutputParameter("evaluations", requiredEvaluations);
        Ranking ranking = new Ranking(population);
        return ranking.getSubfront(0);
    }
}
