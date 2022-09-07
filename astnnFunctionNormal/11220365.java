class BackupThread extends Thread {
    public InteractionUnit(String id) {
        super(id);
        this.setHelpDesc("<br>Interaction among actors can be described in terms of units of interaction. A unit of interaction can be a message passing,<br>a shared tuple read/write, a remote method invocation, an action over the environment,...<br>This entity serves as an abstraction of all the existing ways an agent can interact with another.<br>To characterize interaction units, the user can associate an speech act with each interaction.<br>			");
        this.setHelpRecom("");
    }
}
