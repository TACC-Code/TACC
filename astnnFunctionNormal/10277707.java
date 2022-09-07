class BackupThread extends Thread {
    protected void doServletGet(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        GregorianCalendar start = getStartDate(req);
        Long starttime = new Long(start.getTimeInMillis());
        int starthr = getStartHour(req);
        int numhrs = Integer.parseInt(GetOption(req, "channel_num_hours", "24"));
        int numhrsdisplayed = Math.min(numhrs, 24 - start.get(Calendar.HOUR_OF_DAY));
        GregorianCalendar end = (GregorianCalendar) start.clone();
        end.add(Calendar.HOUR_OF_DAY, numhrsdisplayed);
        Long endtime = new Long(end.getTimeInMillis());
        int numdays = Integer.parseInt(GetOption(req, "channel_num_days", "7"));
        Vector<?> channels = getChannels(req, 1);
        Vector<?> days = getDays(req, numdays);
        htmlHeaders(resp);
        noCacheHeaders(resp);
        PrintWriter out = getGzippedWriter(req, resp);
        try {
            xhtmlHeaders(out);
            out.println("<head>");
            jsCssImport(req, out);
            out.println("<title>TV Guide</title>");
            out.println("</head>");
            out.println("<body>");
            printTitle(out, "TV guide");
            out.println("<div id=\"content\">");
            SimpleDateFormat fmt = new SimpleDateFormat("EEE, MMM d");
            int cellheight = Integer.parseInt(GetOption(req, "channel_cell_height", "120"));
            int cellwidth = Integer.parseInt(GetOption(req, "channel_cell_width", "120"));
            boolean showChannelLogos = GetOption(req, "UseChannelLogos", "true").equalsIgnoreCase("true");
            boolean showTimes = GetOption(req, "channel_show_time", "true").equalsIgnoreCase("true");
            boolean showDesc = GetOption(req, "channel_show_desc", "false").equalsIgnoreCase("true");
            if ((channels != null) && (channels.size() > 0)) {
                Object channel = channels.get(0);
                printDayHourChannelSelectors(out, req, numhrs);
                Object RecSchedList = SageApi.Api("GetScheduledRecordings");
                out.println("<table cellspacing=\"0\" class=\"epglist\"><tr>");
                out.println("<td>\r\n" + "<table cellspacing=\"0\" class=\"timcol\">");
                out.println("   <tr><td class=\"datecell\"><div>\r\n" + "      <a href=\"" + buildLink(req, 0, 0, -1) + "\" title=\"Previous Channel\"><img class=\"prevday\" alt=\"Arrow Left\" src=\"left.gif\"/></a>\r\n");
                out.println("      " + SageApi.Api("GetChannelNumber", new Object[] { channel }).toString() + " - ");
                String chname = SageApi.Api("GetChannelName", new Object[] { channel }).toString();
                if (showChannelLogos && null != SageApi.Api("GetChannelLogo", channel)) {
                    String chID = SageApi.Api("GetStationID", new Object[] { channel }).toString();
                    out.println("<img style=\"height: 2.5em\" src=\"ChannelLogo?ChannelID=" + chID + "&type=Med&index=1&fallback=true\" alt=\"" + Translate.encode(chname) + " logo\" title=\"" + Translate.encode(chname) + "\"/>");
                } else {
                    out.println(Translate.encode(chname));
                }
                out.println("      <a href=\"" + buildLink(req, 0, 0, 1) + "\" title=\"Next Channel\"><img class=\"nextday\" alt=\"Arrow Right\" src=\"right.gif\"/></a>\r\n" + "   </div></td></tr>");
                GregorianCalendar hours = (GregorianCalendar) start.clone();
                DateFormat timefmt = DateFormat.getTimeInstance(DateFormat.SHORT);
                for (int i = 0; i < numhrsdisplayed; i++) {
                    out.println("   <tr><td class=\"timecell\"><div  style=\"height:" + Long.toString(cellheight - 8) + "px\">");
                    if (i == 0 && hours.get(Calendar.HOUR_OF_DAY) > 0) {
                        out.println("      <a href=\"" + buildLink(req, 0, -Math.min(numhrsdisplayed, hours.get(Calendar.HOUR_OF_DAY)), 0) + "\" title=\"Earlier\"><img class=\"earlier\" alt=\"Arrow Up\" src=\"up.gif\"/></a>");
                    }
                    if (i == numhrsdisplayed - 1 && hours.get(Calendar.HOUR_OF_DAY) < 23) {
                        out.println("      <a href=\"" + buildLink(req, 0, Math.min(numhrsdisplayed, 23 - hours.get(Calendar.HOUR_OF_DAY)), 0) + "\" title=\"Later\"><img class=\"later\" alt=\"Arrow Down\" src=\"down.gif\"/></a>");
                    }
                    out.println(timefmt.format(hours.getTime()));
                    out.println("   </div></td></tr>");
                    hours.add(Calendar.HOUR_OF_DAY, 1);
                }
                out.println("</table></td>");
                boolean markHDTV = getMarkHDTV(req);
                boolean markFirstRuns = getMarkFirstRuns(req);
                boolean showEpisodeID = GetOption(req, "ShowEpisodeID", "false").equalsIgnoreCase("true");
                for (int i = 0; i < days.size(); i++) {
                    GregorianCalendar day = (GregorianCalendar) days.get(i);
                    out.println("<td><table cellspacing=\"0\" class=\"channelcol\"><tr>");
                    out.println("   <td class=\"channelid\"><div style=\"width:" + Long.toString(cellwidth) + "px;\">");
                    out.println(fmt.format(day.getTime()));
                    if (i == 0) out.println("      <a href=\"" + buildLink(req, -days.size(), 0, 0) + "\" title=\"page left\"><img alt=\"Arrow Left\" class=\"pageleft\" src=\"left.gif\"/></a>");
                    if (i == days.size() - 1) out.print("      <a href=\"" + buildLink(req, days.size(), 0, 0) + "\" title=\"page right\"><img alt=\"Arrow Right\"  class=\"pageright\" src=\"right.gif\"/></a>");
                    out.println("</div></td></tr>");
                    Object airings = SageApi.Api("GetAiringsOnChannelAtTime", new Object[] { channel, starttime, endtime, Boolean.FALSE });
                    Object allConflicts = SageApi.Api("GetAiringsThatWontBeRecorded", new Object[] { Boolean.FALSE });
                    Object unresolvedConflicts = SageApi.Api("GetAiringsThatWontBeRecorded", new Object[] { Boolean.TRUE });
                    long skippedDuration = 0;
                    for (int j = 0; j < SageApi.Size(airings); j++) {
                        Airing airing = new Airing(SageApi.GetElement(airings, j));
                        long cellstart = airing.getStartDate().getTime();
                        if (cellstart < starttime.longValue()) cellstart = starttime.longValue();
                        long cellend = airing.getEndDate().getTime();
                        if (cellend > endtime.longValue()) cellend = endtime.longValue();
                        long thiscellheight = (((skippedDuration + cellend - cellstart) / 1000 / 60) * cellheight / 60) - 8;
                        String ep = airing.getEpisode();
                        if (ep != null && ep.trim().length() == 0) ep = null;
                        if (showEpisodeID && (airing.idType == Airing.ID_TYPE_AIRING || (airing.idType == Airing.ID_TYPE_MEDIAFILE && SageApi.booleanApi("IsTVFile", new Object[] { airing.sageAiring })))) {
                            String epId = (String) SageApi.Api("GetShowExternalID", airing.sageAiring);
                            int epIdx = epId.length() - 4;
                            if (epId != null && epId.length() >= 12 && !epId.substring(epIdx).matches("^0*$")) if (ep != null) ep = epId.substring(epIdx) + " - " + ep; else ep = epId.substring(epIdx);
                        }
                        String titletext = airing.getTitle();
                        String alttext = Translate.encode(titletext);
                        if (titletext.trim().length() == 0) titletext = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"; else titletext = Translate.encode(titletext);
                        if (ep != null && !ep.equals("")) {
                            ep = Translate.encode(ep);
                            alttext = titletext + "\r\n" + ep;
                            titletext += "<br/>" + ep;
                        }
                        if (thiscellheight <= 0) {
                            skippedDuration += cellend - cellstart;
                        } else {
                            skippedDuration = 0;
                            String desc = "";
                            if (showDesc) {
                                desc = SageApi.StringApi("GetShowDescription", new Object[] { airing.sageAiring });
                                if (desc != null && desc.length() > 0) {
                                    if (desc.length() > 150) desc = desc.substring(0, 150) + "...";
                                }
                            }
                            out.println("   <tr><td class=\"epgcell\">");
                            out.println("   <table class=\"" + airing.getBgClassName(true) + "\" cellspacing=\"0\"><tr>\r\n" + "<td class=\"" + airing.getBorderClassName() + "\"\r\n" + "title=\"" + alttext + "\r\n" + timefmt.format(airing.getStartDate()) + " - " + timefmt.format(airing.getEndDate()) + "\r\n" + desc + "\"" + ">");
                            if (airing.getWatched()) out.print("   <div class=\"watched\""); else out.print("   <div class=\"\"");
                            out.println(" style=\"width:" + Long.toString(cellwidth) + "px; height:" + Long.toString(thiscellheight) + "px\">");
                            if (markHDTV || markFirstRuns) {
                                out.print("     <img src=\"MarkerHDTVDot.gif\" class=\"hd\" title=\"High Definition\"");
                                if (!airing.getHDTV() || !markHDTV) {
                                    out.print(" style=\"visibility: hidden;\"");
                                }
                                out.println("></img>");
                                out.print("     <img src=\"MarkerFirstRunDot.gif\" class=\"firstrun\" title=\"First Run\"");
                                if (!airing.getFirstRun() || !markFirstRuns) {
                                    out.print(" style=\"visibility: hidden;\"");
                                }
                                out.println("></img>");
                                out.println("<br/>");
                            }
                            out.print("<a " + "title=\"" + alttext + "\r\n" + timefmt.format(airing.getStartDate()) + " - " + timefmt.format(airing.getEndDate()) + "\r\n" + desc + "\" href=\"DetailedInfo?" + airing.getIdArg() + "\">");
                            if (1 == SageApi.Size(SageApi.Api("DataIntersection", new Object[] { RecSchedList, airing.sageAiring }))) {
                                out.print("<img src=\"recording.gif\" class=\"RecordingIndicator\" alt=\"scheduled to record\"/>");
                            } else {
                                if (SageApi.booleanApi("IsManualRecord", new Object[] { airing.sageAiring }) && SageApi.Api("GetMediaFileForAiring", airing.sageAiring) == null) {
                                    out.print("<img src=\"conflicticon.gif\" class=\"UnresolvedConflictIndicator\" alt=\"Unresolved Conflict\" title=\"Unresolved Conflict\"/>");
                                } else if (1 == SageApi.Size(SageApi.Api("DataIntersection", new Object[] { allConflicts, airing.sageAiring }))) {
                                    if (1 == SageApi.Size(SageApi.Api("DataIntersection", new Object[] { unresolvedConflicts, airing.sageAiring }))) {
                                        out.print("<img src=\"conflicticon.gif\" class=\"UnresolvedConflictIndicator\" alt=\"Unresolved Conflict\" title=\"Unresolved Conflict\"/>");
                                    } else {
                                        out.print("<img src=\"resolvedconflicticon.gif\" class=\"ResolvedConflictIndicator\" alt=\"Resolved Conflict\" title=\"Resolved Conflict\"/>");
                                    }
                                }
                            }
                            out.println(titletext + "</a><br/>\r\n");
                            if (showTimes) {
                                out.println("<br/>" + timefmt.format(airing.getStartDate()) + " - " + timefmt.format(airing.getEndDate()));
                            }
                            if (showDesc) {
                                out.println("<br/>" + Translate.encode(desc));
                            }
                            out.println("   </div></td></tr></table></td></tr>");
                        }
                    }
                    starttime = new Long(starttime.longValue() + 24 * 60 * 60 * 1000);
                    endtime = new Long(endtime.longValue() + 24 * 60 * 60 * 1000);
                    out.println("</table></td>");
                }
            }
            out.println("</tr></table>");
            out.println("<div class=\"exphideall\">\r\n" + "<a onclick=\"javascript:showOptions()\" href=\"#Options\">[Show Options]</a></div>");
            out.println("<div id=\"options\" class=\"options\">\r\n" + "<h2><a name=\"Options\">EPG Channel Options:</a></h2>\r\n" + "<form method='get' action='SetOptions'>\r\n" + "<input type=\"hidden\" name=\"returnto\" value=\"" + req.getRequestURI() + "?" + req.getQueryString() + "\"/>\r\n" + "<dl><dt>Cell width (pixels)</dt><dd>");
            PrintOptionsDropdown(req, out, "channel_cell_width", Integer.toString(cellwidth), CELL_SIZE_OPTS);
            out.println("</dd><dt>Cell height (pixels/hr)</dt><dd>");
            PrintOptionsDropdown(req, out, "channel_cell_height", Integer.toString(cellwidth), CELL_SIZE_OPTS);
            out.println("</dd><dt>First hour to display (overridden by choice at the top of the page)</dt><dd>");
            PrintOptionsDropdown(req, out, "channel_first_hour", Integer.toString(starthr), FIRST_HR_OPTS);
            out.println("</dd><dt>Number of hours to display</dt><dd>");
            PrintOptionsDropdown(req, out, "channel_num_hours", Integer.toString(numhrs), NUM_HRS_OPTS);
            out.println("</dd><dt>Number of days to display</dt><dd>");
            PrintOptionsDropdown(req, out, "channel_num_days", Integer.toString(numdays), NUM_DAYS_OPTS);
            out.println("</dd><dt>Channel&nbsp;Logos:&nbsp;</dt><dd>");
            PrintOptionsDropdown(req, out, "UseChannelLogos", "true", ENABLE_DISABLE_OPTS);
            out.println("</dd><dt>Show start/end times</dt><dd>");
            PrintOptionsDropdown(req, out, "channel_show_time", "false", ENABLE_DISABLE_OPTS);
            out.println("</dd><dt>Show description</dt><dd>");
            PrintOptionsDropdown(req, out, "channel_show_desc", "false", ENABLE_DISABLE_OPTS);
            out.println("</dd><dt>Show&nbsp;HDTV&nbsp;Marker</dt><dd>");
            PrintOptionsDropdown(req, out, "epg_mark_hdtv", "##AsSageTV##", EPG_MARKER_OPTS);
            out.println("</dd><dt>Show&nbsp;First&nbsp;Run&nbsp;Marker</dt><dd>");
            PrintOptionsDropdown(req, out, "epg_mark_first_runs", "##AsSageTV##", EPG_MARKER_OPTS);
            out.println("</dd><dt>Include&nbsp;EpisodeID:&nbsp;</dt><dd>");
            PrintOptionsDropdown(req, out, "ShowEpisodeID", "false", ENABLE_DISABLE_OPTS);
            out.println("</dd></dl><noscript><input type=\"submit\" value=\"SetOptions\"/></noscript></form>\r\n");
            out.println("</div>\r\n" + "<script type=\"text/javascript\">hideOptions();</script>");
            out.println();
            printFooter(req, out);
            out.println("</div>");
            printMenu(req, out);
            out.println("</body></html>");
            out.close();
        } catch (Throwable e) {
            if (!resp.isCommitted()) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.setContentType("text/html");
            }
            out.println();
            out.println();
            out.println("<body><pre>");
            out.println("Exception while processing servlet:\r\n" + e.toString());
            e.printStackTrace(out);
            out.println("</pre>");
            out.close();
            log("Exception while processing servlet", e);
        }
    }
}
