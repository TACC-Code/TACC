class BackupThread extends Thread {
    public void commandAction(Command command, Displayable displayable) {
        if (displayable == main) {
            if (command == quitCommand) {
                exitMIDlet();
            } else if (command == channelCommand) {
                getDisplay().setCurrent(getChannelScreen());
            } else if (command == aboutCommand) {
                getDisplay().setCurrent(getAboutScreen());
            } else if (command == addCommand) {
                getDisplay().setCurrent(getAddScreen());
            }
        } else if (displayable == item) {
            if (command == backCommand) {
                getDisplay().setCurrent(getChannelScreen());
            }
        } else if (displayable == channel) {
            if (command == refreshCommand) {
                ChannelVO channel = getChannelDAO().load(getSelectedChannel());
                getRSSParser().parse(channel.getUrl());
            } else if (command == itemCommand) {
                getDisplay().setCurrent(getItemScreen());
            } else if (command == backCommand) {
                getDisplay().setCurrent(getMainScreen());
            }
        } else if (displayable == about) {
            if (command == backCommand) {
                getDisplay().setCurrent(getMainScreen());
            }
        } else if (displayable == add) {
            if (command == backCommand) {
                getDisplay().setCurrent(getMainScreen());
            } else if (command == addCommand) {
                String url = add.getString(add.find("url"), "text", "").toLowerCase();
                ChannelVO channel = insertChannel(url);
                updateMainScreen();
                parseChannel(channel);
                getDisplay().setCurrent(getMainScreen());
            }
        }
    }
}
