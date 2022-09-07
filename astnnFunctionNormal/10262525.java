class BackupThread extends Thread {
    public String[] getChannelUsers() {
        if (isInChannel()) {
            java.util.ArrayList<String> users = new java.util.ArrayList<String>();
            RSComponent[] comps = methods.interfaces.getComponent(INTERFACE_CLAN_CHAT, INTERFACE_CLAN_CHAT_CHANNEL_USERS_LIST).getComponents();
            for (RSComponent comp : comps) {
                String text = comp.getText();
                if (text == null || text.isEmpty()) {
                    continue;
                } else if (comp.getRelativeX() >= 36) {
                    continue;
                }
                String user = text.trim();
                if (user.endsWith("...")) {
                    String[] actions = comp.getActions();
                    if (actions != null) {
                        for (String action : actions) {
                            if (action != null && action.toLowerCase().matches("^(add|remove)")) {
                                user = action.substring(action.indexOf(32) + 1);
                                user = user.substring(user.indexOf(32)).trim();
                                break;
                            }
                        }
                    }
                }
                users.add(user);
            }
            return users.toArray(new String[users.size()]);
        }
        return null;
    }
}
