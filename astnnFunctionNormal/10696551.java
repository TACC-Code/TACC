class BackupThread extends Thread {
    public SolutionSet execute() throws JMException {
        int populationSize, archiveSize, maxEvaluations, evaluations;
        Operator crossoverOperator, mutationOperator, selectionOperator;
        SolutionSet solutionSet, archive, offSpringSolutionSet;
        QualityIndicator indicators;
        populationSize = ((Integer) getInputParameter("populationSize")).intValue();
        archiveSize = ((Integer) getInputParameter("archiveSize")).intValue();
        maxEvaluations = ((Integer) getInputParameter("maxEvaluations")).intValue();
        indicators = ((QualityIndicator) getInputParameter("indicators"));
        crossoverOperator = operators_.get("crossover");
        mutationOperator = operators_.get("mutation");
        selectionOperator = operators_.get("selection");
        solutionSet = new SolutionSet(populationSize);
        archive = new SolutionSet(archiveSize);
        evaluations = 0;
        Solution newSolution;
        for (int i = 0; i < populationSize; i++) {
            newSolution = new Solution(problem_);
            problem_.evaluate(newSolution);
            problem_.evaluateConstraints(newSolution);
            evaluations++;
            solutionSet.add(newSolution);
        }
        try {
            FileOutputStream fos = new FileOutputStream("metrics" + ".spea2ss");
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            while (evaluations < maxEvaluations) {
                SolutionSet union = ((SolutionSet) solutionSet).union(archive);
                Spea2Fitness spea = new Spea2Fitness(union);
                spea.fitnessAssign();
                archive = spea.environmentalSelection(archiveSize);
                offSpringSolutionSet = new SolutionSet(populationSize);
                Solution[] parents = new Solution[2];
                int j = 0;
                do {
                    j++;
                    parents[0] = (Solution) selectionOperator.execute(archive);
                } while (j < SPEA2.TOURNAMENTS_ROUNDS);
                int k = 0;
                do {
                    k++;
                    parents[1] = (Solution) selectionOperator.execute(archive);
                } while (k < SPEA2.TOURNAMENTS_ROUNDS);
                Solution[] offSpring = (Solution[]) crossoverOperator.execute(parents);
                mutationOperator.execute(offSpring[0]);
                problem_.evaluate(offSpring[0]);
                problem_.evaluateConstraints(offSpring[0]);
                offSpringSolutionSet.add(offSpring[0]);
                evaluations++;
                solutionSet = offSpringSolutionSet;
                if ((evaluations % 100 == 0) && (indicators != null)) {
                    SolutionSet nonDominatedTmp = (new Ranking(archive)).getSubfront(0);
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
        } catch (Exception e) {
            Configuration.logger_.severe("Error acceding to the file");
            e.printStackTrace();
        }
        Ranking ranking = new Ranking(archive);
        return ranking.getSubfront(0);
    }
}
