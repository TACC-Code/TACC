class BackupThread extends Thread {
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        ChannelManager manager = ChannelManager.getInstance();
        if ("new".equals(request.getParameter("action"))) {
            ChannelConfig config = new ChannelConfig();
            config.setSettings(new Settings());
            config.setDescription("");
            int i = 1;
            while (name == null) {
                if (manager.getChannel("tetrinet" + i) == null) {
                    name = "tetrinet" + i;
                } else {
                    i++;
                }
            }
            config.setName(name);
            manager.createChannel(config);
        } else {
            Channel channel = manager.getChannel(name);
            ChannelConfig config = channel.getConfig();
            String password = request.getParameter("password");
            password = password == null ? null : password.trim();
            password = "".equals(password) ? null : password;
            config.setDescription(request.getParameter("description"));
            config.setAccessLevel(Integer.parseInt(request.getParameter("accessLevel")));
            config.setPassword(password);
            config.setMaxPlayers(max(0, min(6, Integer.parseInt(request.getParameter("maxPlayers")))));
            config.setMaxSpectators(max(0, Integer.parseInt(request.getParameter("maxSpectators"))));
            config.setPersistent("true".equals(request.getParameter("persistent")));
            config.setVisible("true".equals(request.getParameter("visible")));
            config.setIdleAllowed("true".equals(request.getParameter("idle")));
            config.setWinlistId(request.getParameter("winlist"));
            config.setTopic(request.getParameter("topic"));
            config.setSpeed(Speed.valueOf(request.getParameter("speed").toUpperCase()));
        }
        response.sendRedirect("/channel.jsp?name=" + name);
        Server.getInstance().getConfig().save();
    }
}
