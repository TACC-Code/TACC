class BackupThread extends Thread {
    void handleAgentCompleteEvent(AgentCompleteEvent event) {
        AsteriskAgentImpl agent = getAgentByAgentId(event.getChannel());
        if (agent == null) {
            logger.error("Ignored AgentCompleteEvent for unknown agent " + event.getChannel());
            return;
        }
        agent.updateState(AgentState.AGENT_IDLE);
    }
}
