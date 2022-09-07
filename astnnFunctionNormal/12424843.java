class BackupThread extends Thread {
    void handleAgentCalledEvent(AgentCalledEvent event) {
        AsteriskAgentImpl agent = getAgentByAgentId(event.getAgentCalled());
        if (agent == null) {
            logger.error("Ignored AgentCalledEvent for unknown agent " + event.getAgentCalled());
            return;
        }
        updateRingingAgents(event.getChannelCalling(), agent);
        updateAgentState(agent, AgentState.AGENT_RINGING);
    }
}
