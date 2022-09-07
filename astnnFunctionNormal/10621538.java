class BackupThread extends Thread {
    public Command executeCommand(Command c) {
        try {
            connection = url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            os = new ObjectOutputStream(new BufferedOutputStream(connection.getOutputStream()));
            os.writeObject(c);
            os.flush();
            c = (Command) readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return c;
    }
}
