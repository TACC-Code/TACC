class BackupThread extends Thread {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        response.setCharacterEncoding("utf8");
        try {
            IDispatchingChannel d = (IDispatchingChannel) _kernel.getChannel(IDispatchingChannel.class);
            d.dispatch(request, response);
        } catch (ModuleNotFoundException e) {
            throw new ServletException(e);
        }
    }
}
