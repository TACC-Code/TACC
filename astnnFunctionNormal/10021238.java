class BackupThread extends Thread {
                public void removed(String clientId, boolean timeout) {
                    members.values().remove(clientId);
                    Log.info("members: " + members);
                    Channel channel = getBayeux().getChannel(channelName, false);
                    if (channel != null) channel.publish(getClient(), members.keySet(), messageId);
                }
}
