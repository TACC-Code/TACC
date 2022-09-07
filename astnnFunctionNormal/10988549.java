class BackupThread extends Thread {
    public void addCostValues(Id from, Id to, Double time, Double distance, Double cost) {
        CostMatrixKey costMatrixKey = new CostMatrixKey(from, to);
        CostValues costValues = new CostValues(time, distance, cost);
        if (!costMatrix.containsKey(costMatrixKey)) {
            costMatrix.put(costMatrixKey, costValues);
        } else {
            logger.warn("key already exist " + costMatrixKey + " and has been overwrited");
            costMatrix.put(costMatrixKey, costValues);
        }
    }
}
