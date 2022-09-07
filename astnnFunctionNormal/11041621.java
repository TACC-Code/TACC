class BackupThread extends Thread {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            if (!req.getRequestURL().toString().startsWith(ForoNuvoloConstants.FORUM_DOMAIN)) {
                return;
            }
            URL url = getServletContext().getResource("/ganttproject_forums.xml");
            InputStream is = url.openStream();
            if (is == null) {
                resp.getWriter().write("failed to open xml file");
                return;
            }
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            String startParam = req.getParameter("start");
            if (startParam == null) {
                startParam = "0";
            }
            String endParam = req.getParameter("end");
            int start;
            try {
                start = Integer.parseInt(startParam);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Failed to parse request", e);
            }
            String verification = req.getParameter("verify");
            ParsingState state = new ParsingState(start, start + IMPORT_CHUNK, verification != null);
            parser.parse(is, new SFDataHandlerImpl(state));
            resp.getWriter().write("parsing finished\n");
            resp.getWriter().write(state.getStats());
            int nextStart = start + IMPORT_CHUNK;
            if (state.getMessageCount() >= nextStart) {
                try {
                    if (endParam == null || nextStart < Integer.parseInt(endParam)) {
                        TaskOptions options = TaskOptions.Builder.url("/import").param("start", String.valueOf(start + IMPORT_CHUNK)).method(TaskOptions.Method.GET);
                        if (endParam != null) {
                            options = options.param("end", endParam);
                        }
                        if (verification != null) {
                            options = options.param("verify", "");
                        }
                        QueueFactory.getDefaultQueue().add(options);
                    }
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Failed to parse request", e);
                }
            }
            DiscussionsViewPage.resetDiscussionViewPageCache();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
}
