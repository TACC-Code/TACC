class BackupThread extends Thread {
    @Override
    public void onStep(Actor a) {
        if (a instanceof Expedition && !(a instanceof NonPrincipalExpedition)) {
            boolean isGFXUI = UserInterface.getUI() instanceof ExpeditionOryxUI;
            int choice = -1;
            if (isGFXUI) {
                choice = UserInterface.getUI().switchChat("Ships", "What do you want to do?", "Transfer equipment", "Board ships", "Do nothing");
                choice++;
            } else {
                choice = UserInterface.getUI().switchChat("Ships", "What do you want to do?", "Transfer to expedition", "Transfer to ships", "Board ships", "Do nothing");
            }
            switch(choice) {
                case 0:
                    ((ExpeditionUserInterface) UserInterface.getUI()).transferFromCache("Select the goods to transfer", null, this);
                    break;
                case 1:
                    ((ExpeditionUserInterface) UserInterface.getUI()).transferFromExpedition(this);
                    break;
                case 2:
                    if (canCarryWeight(((Expedition) a).getWeightToBoardShip())) {
                        boardShips((Expedition) a);
                    } else {
                        UserInterface.getUI().showMessage("The ships are too full!");
                    }
                    break;
                case 3:
            }
        }
    }
}
