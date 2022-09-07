class BackupThread extends Thread {
    protected Document rawService(Document out, Dictionary parameters, boolean readwrite) throws Exception {
        HttpServletRequest request = (HttpServletRequest) parameters.get("request");
        HttpServletResponse response = (HttpServletResponse) parameters.get("response");
        LogRequestSink trace = null;
        try {
            String suppress = request.getParameter("suppresstrace");
            if (Trace.getInstance().getTrace() && !"yes".equals(suppress)) {
                trace = new LogRequestSink();
                Trace.getInstance().setCurrent(trace);
                trace.setRequest(request);
            }
            String todo = request.getParameter("todo");
            ProcessingInstruction pi = out.createProcessingInstruction("cocoon-format", "type=\"text/xml\"");
            out.appendChild(pi);
            FormatterToDOM builder = new FormatterToDOM(out);
            if ("fetch".equals(todo)) {
                DBWrapper.fetch(builder, request.getParameter("db"), request.getParameter("document"), request.getParameter("pattern"), request.getParameter("fetch-childs"), request.getParameter("fetch-attributes"), request.getParameter("flags"), trace);
            } else if ("archive".equals(todo)) {
                int id = 0;
                String tmp = request.getParameter("id");
                if (tmp != null) {
                    id = Integer.parseInt(tmp);
                }
                DBWrapper.fetchArchive(builder, request.getParameter("db"), request.getParameter("document"), id, request.getParameter("flags"), trace);
            } else if ("write".equals(todo) && readwrite) {
                int pos = 0;
                String[] owner = null;
                if (request.getParameter("owner") != null) {
                    owner = new String[1];
                    owner[0] = request.getParameter("owner");
                }
                String tmp = request.getParameter("position");
                if (tmp != null) {
                    pos = Integer.parseInt(tmp);
                }
                String[] id = DBWrapper.write(request.getParameter("db"), request.getParameter("document"), request.getParameter("xml"), owner, pos, trace);
                AttributeListImpl atts = new AttributeListImpl();
                atts.addAttribute("plid", "CDATA", id[0]);
                builder.startElement("ok", atts);
                builder.endElement("ok");
            } else if ("remove".equals(todo) && readwrite) {
                String[] id = null;
                if (request.getParameter("id") != null) {
                    id = new String[1];
                    id[0] = request.getParameter("id");
                }
                int version = DBWrapper.remove(request.getParameter("db"), request.getParameter("document"), id, trace);
                AttributeListImpl atts = new AttributeListImpl();
                atts.addAttribute("plversion", "CDATA", String.valueOf(version));
                builder.startElement("ok", atts);
                builder.endElement("ok");
            } else {
                throw new Exception("illegal request: " + todo + " write=" + readwrite);
            }
        } catch (Exception ex) {
            if (trace == null) {
                trace = new LogRequestSink();
                Trace.getInstance().setCurrent(trace);
                trace.setRequest(request);
            }
            trace.setException(ex);
            throw ex;
        } finally {
            if (trace != null) {
                finalizeTrace(trace, out);
            }
        }
        return out;
    }
}
