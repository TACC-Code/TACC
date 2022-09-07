class BackupThread extends Thread {
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String name = request.getParameter("name");
        String redirect = "/user.jsp?name=" + name;
        Client client = ClientRepository.getInstance().getClient(name);
        if ("kick".equals(action)) {
            logger.info(client.getUser().getName() + " (" + client.getInetAddress() + ") has been kicked by " + request.getRemoteUser() + " (" + request.getRemoteHost() + ")");
        } else if ("ban".equals(action)) {
            Banlist banlist = Banlist.getInstance();
            banlist.ban(client.getInetAddress().getHostAddress());
            logger.info(client.getUser().getName() + " (" + client.getInetAddress() + ") has been banned by " + request.getRemoteUser() + " (" + request.getRemoteHost() + ")");
            Server.getInstance().getConfig().save();
        }
        client.disconnect();
        response.sendRedirect("/channel.jsp?name=" + client.getChannel().getConfig().getName());
    }
}
