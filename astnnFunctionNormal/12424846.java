class BackupThread extends Thread {
    void handleAgentConnectEvent(AgentConnectEvent event) {
        AsteriskAgentImpl agent = getAgentByAgentId(event.getChannel());
        if (agent == null) {
            logger.error("Ignored AgentConnectEvent for unknown agent " + event.getChannel());
            return;
        }
        agent.updateState(AgentState.AGENT_ONCALL);
    }
}
