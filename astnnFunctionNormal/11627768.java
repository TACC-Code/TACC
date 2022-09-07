class BackupThread extends Thread {
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final boolean isInclude = (request.getAttribute(StaticResourceServlet.INCLUDE_SERVLET_PATH) != null);
        final boolean isForward = (request.getAttribute(StaticResourceServlet.FORWARD_SERVLET_PATH) != null);
        String path = null;
        if (isInclude) {
            path = (String) request.getAttribute(StaticResourceServlet.INCLUDE_SERVLET_PATH);
        } else {
            path = request.getServletPath();
        }
        path = WinstoneRequest.decodeURLToken(path);
        final long cachedResDate = request.getDateHeader(StaticResourceServlet.CACHED_RESOURCE_DATE_HEADER);
        logger.debug("{}: path={}", getServletConfig().getServletName(), path);
        final File res = path.equals("") ? webRoot : new File(webRoot, path);
        if (!res.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, StringUtils.replaceToken("File {} not found", path));
        } else if (!StaticResourceServlet.isDescendant(webRoot, res, webRoot)) {
            logger.debug("Requested path {} was outside the webroot {}", res.getCanonicalPath(), webRoot.toString());
            response.sendError(HttpServletResponse.SC_FORBIDDEN, StringUtils.replaceToken("Illegal path error - {}", path));
        } else if (!isInclude && !isForward && StaticResourceServlet.isDescendant(new File(webRoot, "WEB-INF"), res, webRoot)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, StringUtils.replaceToken("Illegal path error - {}", path));
        } else if (!isInclude && !isForward && StaticResourceServlet.isDescendant(new File(webRoot, "META-INF"), res, webRoot)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, StringUtils.replaceToken("Illegal path error - {}", path));
        } else if (res.isDirectory()) {
            if (path.endsWith("/")) {
                if (directoryList) {
                    generateDirectoryList(request, response, path);
                } else {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access to this resource is denied");
                }
            } else {
                response.sendRedirect(prefix + path + "/");
            }
        } else if (!isInclude && (cachedResDate != -1) && (cachedResDate < ((System.currentTimeMillis() / 1000L) * 1000L)) && (cachedResDate >= ((res.lastModified() / 1000L) * 1000L))) {
            final String mimeType = getServletContext().getMimeType(res.getName().toLowerCase());
            if (mimeType != null) {
                response.setContentType(mimeType);
            }
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            response.setContentLength(0);
            response.flushBuffer();
        } else if ((request.getHeader(StaticResourceServlet.RANGE_HEADER) == null) || isInclude) {
            final String mimeType = getServletContext().getMimeType(res.getName().toLowerCase());
            if (mimeType != null) {
                response.setContentType(mimeType);
            }
            final InputStream resStream = new FileInputStream(res);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentLength((int) res.length());
            response.addDateHeader(StaticResourceServlet.LAST_MODIFIED_DATE_HEADER, res.lastModified());
            OutputStream out = null;
            Writer outWriter = null;
            try {
                out = response.getOutputStream();
            } catch (final IllegalStateException err) {
                outWriter = response.getWriter();
            } catch (final IllegalArgumentException err) {
                outWriter = response.getWriter();
            }
            final byte buffer[] = new byte[4096];
            int read = resStream.read(buffer);
            while (read > 0) {
                if (out != null) {
                    out.write(buffer, 0, read);
                } else {
                    outWriter.write(new String(buffer, 0, read, response.getCharacterEncoding()));
                }
                read = resStream.read(buffer);
            }
            resStream.close();
        } else if (request.getHeader(StaticResourceServlet.RANGE_HEADER).startsWith("bytes=")) {
            final String mimeType = getServletContext().getMimeType(res.getName().toLowerCase());
            if (mimeType != null) {
                response.setContentType(mimeType);
            }
            final InputStream resStream = new FileInputStream(res);
            final List<String> ranges = new ArrayList<String>();
            final StringTokenizer st = new StringTokenizer(request.getHeader(StaticResourceServlet.RANGE_HEADER).substring(6).trim(), ",", Boolean.FALSE);
            int totalSent = 0;
            String rangeText = "";
            while (st.hasMoreTokens()) {
                final String rangeBlock = st.nextToken();
                int start = 0;
                int end = (int) res.length();
                final int delim = rangeBlock.indexOf('-');
                if (delim != 0) {
                    start = Integer.parseInt(rangeBlock.substring(0, delim).trim());
                }
                if (delim != (rangeBlock.length() - 1)) {
                    end = Integer.parseInt(rangeBlock.substring(delim + 1).trim());
                }
                totalSent += (end - start);
                rangeText += "," + start + "-" + end;
                ranges.add(start + "-" + end);
            }
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            response.addHeader(StaticResourceServlet.CONTENT_RANGE_HEADER, "bytes " + rangeText.substring(1) + "/" + res.length());
            response.setContentLength(totalSent);
            response.addHeader(StaticResourceServlet.ACCEPT_RANGES_HEADER, "bytes");
            response.addDateHeader(StaticResourceServlet.LAST_MODIFIED_DATE_HEADER, res.lastModified());
            final OutputStream out = response.getOutputStream();
            int bytesRead = 0;
            for (final Iterator<String> i = ranges.iterator(); i.hasNext(); ) {
                final String rangeBlock = i.next();
                final int delim = rangeBlock.indexOf('-');
                final int start = Integer.parseInt(rangeBlock.substring(0, delim));
                final int end = Integer.parseInt(rangeBlock.substring(delim + 1));
                int read = 0;
                while ((read != -1) && (bytesRead <= res.length())) {
                    read = resStream.read();
                    if ((bytesRead >= start) && (bytesRead < end)) {
                        out.write(read);
                    }
                    bytesRead++;
                }
            }
            resStream.close();
        } else {
            response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
        }
    }
}
