class BackupThread extends Thread {
    public void trackMembers(final Client joiner, final String channelName, Map<String, Object> data, final String messageId) {
        if (Boolean.TRUE.equals(data.get("join"))) {
            Map<String, String> membersMap = _members.get(channelName);
            if (membersMap == null) {
                Map<String, String> newMembersMap = new ConcurrentHashMap<String, String>();
                membersMap = _members.putIfAbsent(channelName, newMembersMap);
                if (membersMap == null) membersMap = newMembersMap;
            }
            final Map<String, String> members = membersMap;
            final String userName = (String) data.get("user");
            members.put(userName, joiner.getId());
            joiner.addListener(new RemoveListener() {

                public void removed(String clientId, boolean timeout) {
                    members.values().remove(clientId);
                    Log.info("members: " + members);
                    Channel channel = getBayeux().getChannel(channelName, false);
                    if (channel != null) channel.publish(getClient(), members.keySet(), messageId);
                }
            });
            Log.info("Members: " + members);
            getBayeux().getChannel(channelName, false).publish(getClient(), members.keySet(), messageId);
        }
    }
}
