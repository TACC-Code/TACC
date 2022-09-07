class BackupThread extends Thread {
    @Override
    public synchronized void ponderHit() {
        if (DEBUGSearch.DEBUG_MODE) {
            if (!searchAdaptorCfg.isPonderingEnabled()) {
                throw new IllegalStateException("searchAdaptorCfg.isPonderingEnabled() = " + searchAdaptorCfg.isPonderingEnabled());
            }
            if (currentGoCommand == null) {
                throw new IllegalStateException("currentGoCommand == null");
            }
            if (!currentGoCommand.isPonder()) {
                throw new IllegalStateException("!currentGoCommand.isPonder()");
            }
        }
        UCISearchMediatorImpl_OpponentPondering ponderMediator = (UCISearchMediatorImpl_OpponentPondering) currentMediator;
        if (currentGoCommand != null) {
            if (currentGoCommand.isPonder()) {
                stopSearch();
            } else {
                if (DEBUGSearch.DEBUG_MODE) throw new IllegalStateException("currentGoCommand.isPonder");
            }
        } else {
            if (DEBUGSearch.DEBUG_MODE) throw new IllegalStateException("currentGoCommand != null");
        }
        boardForSetup.makeMoveForward(revertedMoveForPondering);
        Go go = ponderMediator.getGoCommand();
        go.setPonder(false);
        ITimeController timeController = TimeControllerFactory.createTimeController(searchAdaptorCfg.getTimeConfig(), boardForSetup.getColourToMove(), go);
        currentMediator = new UCISearchMediatorImpl_NormalSearch(ponderMediator.getChannel(), go, timeController, boardForSetup.getColourToMove(), ponderMediator.getBestMoveSender());
        currentGoCommand = go;
        goSearch(false);
    }
}
