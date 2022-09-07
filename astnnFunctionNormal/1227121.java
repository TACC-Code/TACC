class BackupThread extends Thread {
    InputThread(SiteBot bot, Socket socket, BufferedReader breader, BufferedWriter bwriter) {
        _bot = bot;
        _socket = socket;
        _breader = breader;
        _bwriter = bwriter;
        this.setName(bot.getBotName() + "-InputThread");
    }
}
