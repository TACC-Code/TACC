class BackupThread extends Thread {
    public void connect_card(String cardName) throws UserException {
        this.cardName = cardName;
        if (this.connected == false) {
            log.write("Connection to the card : " + cardName + "....");
            if (this.cgd_initiated == false) {
                try {
                    card = new CardGridUser(cardName);
                } catch (PCSCException e) {
                    throw new UserException("CardGridUser : Connection Failure ! " + e.getMessage());
                }
            } else {
                try {
                    card.connect(cardName);
                } catch (PCSCException e) {
                    throw new UserException("CardGridUser : Connection Failure ! " + e.getMessage());
                }
            }
            connected = true;
            log.write("Connection success !");
        } else {
            log.write("I think I'm already connected to " + cardName + " !");
        }
    }
}
