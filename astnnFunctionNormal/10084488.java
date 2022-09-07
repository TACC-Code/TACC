class BackupThread extends Thread {
    public void execute(CommandMessage m) {
        Client client = (Client) m.getSource();
        int min = 1;
        int max = 6;
        if (m.getParameterCount() >= 2) {
            int a = m.getIntParameter(0, min);
            int b = m.getIntParameter(1, max);
            min = min(a, b);
            max = max(a, b);
        } else if (m.getParameterCount() == 1) {
            max = Math.max(min, m.getIntParameter(0, max));
        }
        int result = random.nextInt(abs(max - min) + 1);
        PlineMessage response = new PlineMessage();
        response.setKey("command.random.result", client.getUser().getName(), min, max, (result + min));
        client.getChannel().send(response);
    }
}
