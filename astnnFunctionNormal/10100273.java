class BackupThread extends Thread {
    public void run() {
        try {
            ServerSocket serveur = new ServerSocket(port);
            etatServeur.passeEtat(EtatServeur.DEMARRE);
            Socket socket = serveur.accept();
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            try {
                byte read;
                while (true) {
                    read = input.readByte();
                    output.writeByte(read);
                }
            } catch (EOFException e) {
            }
            output.flush();
            socket.shutdownInput();
            socket.shutdownOutput();
            socket.close();
            serveur.close();
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
    }
}
