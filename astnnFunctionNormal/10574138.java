class BackupThread extends Thread {
    public static void main(String[] args) {
        Server srv = new Server(args[0]);
        srv.connect();
        srv.login(args[1], args[2]);
        Folder inbox = srv.getFolders();
        new FolderProcessor(srv).saveFolder(inbox, "E:/");
        srv.logout();
    }
}
