class BackupThread extends Thread {
    public void run() throws IOException {
        boolean running = true;
        while (running) {
            String result = null;
            String userInput = getInput();
            ArrayList<String> args = new ArrayList<String>(Arrays.asList(userInput.split("\\s")));
            Command command = Command.toCommand(args.remove(0));
            switch(command) {
                case CONNECT:
                    String host = args.get(0);
                    int port = Integer.valueOf(args.get(1));
                    server.connect(host, port);
                    break;
                case DISCONNECT:
                    server.disconnect();
                    break;
                case LOGIN:
                    String username = args.get(0);
                    String password = args.get(1);
                    server.login(username, password);
                    break;
                case LOGOUT:
                    server.logout();
                    break;
                case EXIT:
                    output("Bye");
                    running = false;
                    break;
                case SET:
                    break;
                case HELP:
                    help();
                    break;
                case TD:
                    result = server.query(userInput);
                    break;
                default:
                    throw new AssertionError(command);
            }
            if (result != null) output(result);
        }
        server.logout();
        server.disconnect();
    }
}
