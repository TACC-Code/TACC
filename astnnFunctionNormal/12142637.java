class BackupThread extends Thread {
    protected void doServletGet(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Search search = new Search();
        search.setProperties(req);
        Object searchResults = search.doSearch();
        String timeRangeStr = search.getTimeRange();
        int numprogs = SageApi.Size(searchResults);
        if (searchResults != null) {
            String xml = req.getParameter("xml");
            if (xml != null && xml.equalsIgnoreCase("yes")) {
                SendXmlResult(req, resp, searchResults, "search_results.xml");
                return;
            }
        }
        htmlHeaders(resp);
        noCacheHeaders(resp);
        PrintWriter out = getGzippedWriter(req, resp);
        try {
            xhtmlHeaders(out);
            out.println("<head>");
            jsCssImport(req, out);
            if ((search.getSearchString() != null) || (search.getFavoriteId() != null)) {
                String rssurl = GetRssUrl(req, "Search");
                out.println("<title>Search Results</title>");
                out.println("<link rel=\"alternate\" type=\"application/rss+xml\"" + " href=\"" + rssurl.toString() + "\"" + " title=\"Sage Search RSS feed\"/>");
                out.println("</head>");
                out.println("<body>");
                out.println("<div id=\"title\">" + "<h1><a href=\"index.html\" title=\"home\"><img id=\"logoimg\" src=\"sagelogo.gif\" alt=\"SageTV logo\" title=\"Home Screen\" border=\"0\"/></a>Search Results\r\n" + "<a href=\"" + GetXmlUrl(req) + "\" title=\"Return page in XML\"><img src=\"xml_button.png\" alt=\"[XML]\"/></a>\r\n" + "<a href=\"" + rssurl.toString() + "\" title=\"RSS feed for this page\"><img src=\"rss_button.png\" alt=\"[RSS]\"/></a>\r\n" + "</h1></div>");
                out.println("<div id=\"content\">");
                if (search.getSearchString() != null || search.getFavoriteId() != null) {
                    if (search.isSearchTypeMediaFiles()) {
                        out.print("<p align=\"center\">Searching Media Library for " + Translate.encode(search.getSearchString()) + "</p>");
                    } else {
                        out.print("<p align=\"center\">Searching for ");
                        out.print(Translate.encode(search.getSearchName()));
                        if (search.getFavoriteId() != null || search.isSearchTypeAirings()) {
                            if ("-1".equals(timeRangeStr)) {
                            } else if ("-999".equals(timeRangeStr)) {
                                out.println(" in the Past");
                            } else if ("0".equals(timeRangeStr)) {
                                out.println(" in the Future");
                            } else {
                                long duration = (search.getEndtime().longValue() - search.getStarttime().longValue()) / 1000 / 60 / 60;
                                if (duration > 48 && duration % 24 == 0) out.println(" within the next " + duration / 24 + " days "); else out.println(" within the next " + duration + " hours ");
                            }
                        }
                    }
                }
                out.println("<p align=\"center\">Found " + Integer.toString(numprogs) + " matching airings in DB</p>");
                if (search.isSearchTypeTVFiles() || search.getFavoriteId() != null || search.isSearchTypeAirings()) {
                    Search otherSearch = new Search();
                    otherSearch.setProperties(req);
                    long endTime = otherSearch.getEndtime().longValue();
                    long startTime = otherSearch.getStarttime().longValue();
                    String searchType = otherSearch.getSearchType();
                    int numTV = 0, numPast = 0, numFuture = 0;
                    String URL_nottyperange = req.getRequestURI() + "?" + req.getQueryString().replaceAll("&searchType=[^&]+", "").replaceAll("&TimeRange=[^&]+", "").replaceAll("&page=[0-9]+", "");
                    URL_nottyperange = Translate.encode(URL_nottyperange);
                    if (!searchType.equals(Search.SEARCH_TYPE_TV_FILES)) {
                        otherSearch.setSearchType(Search.SEARCH_TYPE_TV_FILES);
                        Object results = otherSearch.doSearch();
                        numTV = SageApi.Size(results);
                    }
                    System.out.println("start" + startTime);
                    if (!(searchType.equals(Search.SEARCH_TYPE_AIRINGS) && startTime == 0)) {
                        otherSearch.setSearchType(Search.SEARCH_TYPE_AIRINGS);
                        otherSearch.setStarttime(Search.BEGINNING_OF_TIME);
                        otherSearch.setEndtime(Search.NOW);
                        Object results = otherSearch.doSearch();
                        numPast = SageApi.Size(results);
                    }
                    if (!(searchType.equals(Search.SEARCH_TYPE_AIRINGS) && endTime > (System.currentTimeMillis() + 100))) {
                        otherSearch.setSearchType(Search.SEARCH_TYPE_AIRINGS);
                        otherSearch.setStarttime(Search.NOW);
                        otherSearch.setEndtime(Search.END_OF_TIME);
                        Object results = otherSearch.doSearch();
                        numFuture = SageApi.Size(results);
                    }
                    if (numTV > 0 || numPast > 0 || numFuture > 0) {
                        out.println("<p align=\"center\">(Also found:");
                        if (numTV > 0) {
                            out.println(" <a href=\"" + URL_nottyperange + "&amp;searchType=" + Search.SEARCH_TYPE_TV_FILES + "\">" + numTV + " recorded TV files</a>");
                        }
                        if (numPast > 0) {
                            if (numTV > 0) out.print(";");
                            out.println(" <a href=\"" + URL_nottyperange + "&amp;searchType=" + Search.SEARCH_TYPE_AIRINGS + "&amp;TimeRange=-999" + "\">" + numPast + " past Airings</a>");
                        }
                        if (numFuture > 0) {
                            if (numTV > 0 || numPast > 0) out.print(";");
                            out.println(" <a href=\"" + URL_nottyperange + "&amp;searchType=" + Search.SEARCH_TYPE_AIRINGS + "&amp;TimeRange=0" + "\">" + numFuture + " future Airings</a>");
                        }
                        out.println(")</p>");
                    }
                }
                if (numprogs > 0) {
                    out.println("<form name=\"AiringsForm\" method=\"get\" action=\"AiringCommand\">");
                    out.print("<input type=\"hidden\" name=\"returnto\" value=\"" + Translate.encode(req.getRequestURI() + "?" + req.getQueryString()) + "\"/>");
                    boolean showFileSize = (GetOption(req, "ShowFileSize", "true").equalsIgnoreCase("true") && !search.isSearchTypeAirings());
                    int res_per_page = 0;
                    String s = req.getParameter("pagelen");
                    if (s != null) {
                        if (s.equals("inf")) res_per_page = Integer.MAX_VALUE; else try {
                            res_per_page = Integer.parseInt(s);
                        } catch (NumberFormatException e) {
                        }
                    }
                    String groupMethod = req.getParameter("grouping");
                    Object RecSchedList = SageApi.Api("GetScheduledRecordings");
                    Object allConflicts = SageApi.Api("GetAiringsThatWontBeRecorded", new Object[] { Boolean.FALSE });
                    Object unresolvedConflicts = SageApi.Api("GetAiringsThatWontBeRecorded", new Object[] { Boolean.TRUE });
                    AiringList.PrintPagedGroupedAiringList(this, out, searchResults, req, groupMethod, (search.isSearchTypeTVFiles() || search.isSearchTypeAirings()), true, showFileSize, RecSchedList, allConflicts, unresolvedConflicts, res_per_page);
                    out.println("Action on selected:");
                    if (search.isSearchTypeAirings()) {
                        out.println("<select name=\"command\">");
                        printOptionsList(out, AiringCommandServlet.AIRING_ACTIONS_OPT, null);
                        out.println("</select><input type=\"submit\" value=\"Set\"/>");
                    } else if (search.isSearchTypeTVFiles()) {
                        out.println("<select name=\"command\">");
                        printOptionsList(out, AiringCommandServlet.MEDIAFILE_ACTIONS_OPT, null);
                        out.println("</select><input type=\"submit\" value=\"Set\"/>");
                    }
                    out.println("</div>");
                    out.println("</form>");
                } else {
                }
            } else {
                out.println("<title>Search Sage Database</title>");
                out.println("</head>");
                out.println("<body onload=\"document.SearchForm.SearchString.focus();\">");
                printTitle(out, "Search");
                out.println("<div id=\"content\">");
            }
            out.println("<div id=\"options\" class=\"options\" style=\"display: block;\">" + "<form name=\"SearchForm\" method=\"get\" action=\"" + req.getRequestURI() + "\">");
            out.print("<table>");
            if (search.getFavoriteId() == null) {
                out.print("<tr><td>Search String:</td><td><input tabindex=\"0\" type=\"text\" name=\"SearchString\" style=\"width: 30em;\" ");
                if (search.getSearchString() != null) out.println("value=\"" + Translate.encode(search.getSearchString()) + "\"/>"); else out.println("value=\"\"/>");
            } else {
                String faveName = "ID:" + search.getFavorite();
                try {
                    Favorite favorite = new Favorite(Integer.parseInt(search.getFavoriteId()));
                    if (favorite != null) {
                        faveName = favorite.getDescription();
                    }
                } catch (Exception e) {
                }
                out.print("<tr><td>Searching for Favorite:</td><td>" + Translate.encode(faveName));
                out.println("<input type=\"hidden\" name=\"favoriteId\" value=\"" + Translate.encode(search.getFavoriteId()) + "\"/>");
            }
            out.print("</td></tr>\r\n");
            out.print("<tr><td>Search Type:</td><td>");
            out.print("<input type=\"radio\" name=\"searchType\" value=\"Airings\" ");
            if (search.isSearchTypeAirings()) out.print(" checked=\"checked\" ");
            out.println("/>Search for EPG Airings<br/>");
            out.print("<input type=\"radio\" name=\"searchType\" value=\"TVFiles\" ");
            if (search.isSearchTypeTVFiles()) out.print(" checked=\"checked\" ");
            out.println("/>Search for Recorded TV Files<br/>");
            if (search.getFavoriteId() == null) {
                out.print("<table><tr><td><input type=\"radio\" name=\"searchType\" value=\"MediaFiles\" ");
                if (search.isSearchTypeMediaFiles()) out.print(" checked=\"checked\" ");
                out.println("/>Search for Imported Media Files:</td><td>");
                out.print("<input type=\"checkbox\" name=\"Video\" ");
                if (search.isVideo() || !search.isSearchTypeMediaFiles()) out.print(" checked=\"checked\" ");
                out.println("/>Video<br/>");
                out.print("<input type=\"checkbox\" name=\"DVD\" ");
                if (search.isDVD() || !search.isSearchTypeMediaFiles()) out.print(" checked=\"checked\" ");
                out.println("/>DVD<br/>");
                out.print("<input type=\"checkbox\" name=\"Music\" ");
                if (search.isMusic()) out.print(" checked=\"checked\" ");
                out.println("/>Music<br/>");
                out.print("<input type=\"checkbox\" name=\"Picture\" ");
                if (search.isPicture()) out.print(" checked=\"checked\" ");
                out.println("/>Picture</td></tr></table>");
                out.print("<input type=\"checkbox\" name=\"ExactTitle\" ");
                if (search.isExactTitle()) out.print(" checked=\"checked\" ");
                out.println("/> Matching title exactly");
                out.println("</td></tr>\r\n<tr><td>\r\n" + "Search in fields:\r\n" + "</td><td><table><tr><td>");
                out.println("<select name=\"search_fields\" multiple=\"multiple\" size=\"5\">");
                String[] fields = search.getFields();
                List<String> fields_l = null;
                if (fields != null) fields_l = Arrays.asList(fields); else fields_l = new Vector<String>();
                out.print("   <option value=\"title\" ");
                if (fields == null || fields_l.contains("title")) out.print(" selected=\"selected\"");
                out.println(">Title / Album name</option>");
                out.print("   <option value=\"episode\" ");
                if (fields_l.contains("episode")) out.print(" selected=\"selected\"");
                out.println(">Episode / track Name</option>");
                out.print("   <option value=\"desc\" ");
                if (fields_l.contains("desc")) out.print(" selected=\"selected\"");
                out.println(">Description</option>");
                out.print("   <option value=\"people\" ");
                if (fields_l.contains("people")) out.print(" selected=\"selected\"");
                out.println(">People / track Artist</option>");
                out.print("   <option value=\"category\" ");
                if (fields_l.contains("category")) out.print(" selected=\"selected\"");
                out.println(">Category</option>");
                out.print("   <option value=\"rated\" ");
                if (fields_l.contains("rated")) out.print(" selected=\"selected\"");
                out.println(">Rating</option>");
                out.print("   <option value=\"extrated\" ");
                if (fields_l.contains("extrated")) out.print(" selected=\"selected\"");
                out.println(">Extended Rating</option>");
                out.print("   <option value=\"year\" ");
                if (fields_l.contains("year")) out.print(" selected=\"selected\"");
                out.println(">Year</option>");
                out.print("   <option value=\"misc\" ");
                if (fields_l.contains("misc")) out.print(" selected=\"selected\"");
                out.println(">Misc</option>");
                out.print("   <option value=\"**ALL**\" ");
                if (fields_l.contains("**ALL**")) out.print(" selected=\"selected\"");
                out.println(">ALL fields</option>");
                out.println("</select>\r\n" + "</td><td>(use CTRL-Click to select multiple categories)<br/>Ignored for exact<br/>title searches</td></tr></table></td></tr>");
            }
            out.print("<tr><td>Filename:</td><td><table><tr><td><input type=\"text\" name=\"filename\" style=\"width: 30em;\" ");
            if (search.getFilename() != null) out.println("value=\"" + Translate.encode(search.getFilename()) + "\"/>"); else out.println("value=\"\"/>");
            out.print("</td>\r\n");
            out.print("<td><input type=\"checkbox\" name=\"regex\" ");
            if (search.isRegex()) out.print(" checked=\"checked\" ");
            out.println("/>Regular Expression</td>");
            out.print("<td><input type=\"checkbox\" name=\"casesensitive\" ");
            if (search.isCaseSensitive()) out.print(" checked=\"checked\" ");
            out.println("/>Case Sensitive</td></tr></table></td></tr>\r\n");
            out.println("<tr><td colspan=\"2\"><input type=\"submit\" value=\"Search\"/><hr/></td></tr>");
            out.println("<tr><td colspan=\"2\">Filter results:</td></tr>" + "<tr><td>\r\n" + "Find programs occurring:\r\n" + "</td><td>\r\n");
            out.println("<select name=\"TimeRange\">");
            if (timeRangeStr == null) timeRangeStr = "0";
            SageServlet.printOptionsList(out, TIME_FILTER_OPTS, timeRangeStr);
            out.println("</select> (ignored for Media or TV File search)\r\n" + "</td></tr>\r\n<tr><td>\r\n" + "Limit to categories:\r\n" + "</td><td><table><tr><td>");
            out.println("<select name=\"Categories\" multiple=\"multiple\" size=\"10\">");
            String[] Categories = search.getCategories();
            List<String> Categories_l = null;
            if (Categories != null) Categories_l = Arrays.asList(Categories); else Categories_l = new Vector<String>();
            out.print("   <option value=\"**Any**\" ");
            if (Categories == null || Categories.length == 0 || Categories[0].equals("**Any**")) out.print(" selected=\"selected\"");
            out.println(">Any</option>");
            Object categlist = SageApi.Api("GetAllCategories");
            categlist = SageApi.Api("Sort", new Object[] { categlist, Boolean.FALSE, null });
            int numcategs = SageApi.Size(categlist);
            for (int i = 0; i < numcategs; i++) {
                Object categ = SageApi.GetElement(categlist, i);
                out.print("   <option value=\"" + Translate.encode(categ.toString()) + "\"");
                if (Categories != null && Categories.length > 0 && Categories_l.contains(categ.toString())) out.print(" selected=\"selected\"");
                out.println(">" + Translate.encode(categ.toString()) + "</option>");
            }
            out.println("</select>\r\n" + "</td><td>(use CTRL-Click to select multiple categories)</td></tr></table></td></tr>\r\n<tr><td>\r\n" + "Limit to Channel:\r\n" + "</td><td><table><tr><td>");
            out.println("<select name=\"Channels\" multiple=\"multiple\" size=\"10\">");
            String[] Channels = search.getChannels();
            List<String> Channels_l = null;
            if (Channels != null) Channels_l = Arrays.asList(Channels); else Channels_l = new Vector<String>();
            out.print("   <option value=\"**Any**\" ");
            if (Channels == null || Channels.length == 0 || Channels[0].equals("**Any**")) out.print(" selected=\"selected\"");
            out.println(">Any</option>");
            Object channellist = SageApi.Api("GetAllChannels");
            channellist = SageApi.Api("Sort", new Object[] { channellist, Boolean.FALSE, "GetChannelNumber" });
            int numchans = SageApi.Size(channellist);
            for (int i = 0; i < numchans; i++) {
                Object chan = SageApi.GetElement(channellist, i);
                if (SageApi.booleanApi("IsChannelViewable", new Object[] { chan })) {
                    String channame = SageApi.StringApi("GetChannelName", new Object[] { chan });
                    String channumber = SageApi.StringApi("GetChannelNumber", new Object[] { chan });
                    String chanId = Integer.toString(SageApi.IntApi("GetStationID", new Object[] { chan }));
                    out.print("   <option value=\"" + chanId + "\"");
                    if (Channels != null && Channels.length > 0 && Channels_l.contains(chanId)) out.print(" selected=\"selected\"");
                    out.println(">" + Translate.encode(channumber + " -- " + channame) + "</option>");
                }
            }
            out.println("</select>\r\n" + "</td><td>(use CTRL-Click to select multiple channels)</td></tr></table></td></tr>\r\n<tr><td>\r\n" + "Watched:</td><td><select name=\"watched\">");
            String opt = search.getWatched();
            SageServlet.printOptionsList(out, WATCHED_OPTS, opt);
            out.println("</select>\r\n" + "</td></tr>\r\n<tr><td>\r\n" + "Don't Like:</td><td><select name=\"dontlike\">");
            opt = search.getDontlike();
            SageServlet.printOptionsList(out, SET_CLEAR_OPTS, opt);
            out.println("</select>\r\n" + "</td></tr>\r\n<tr><td>\r\n" + "Favorite:</td><td><select name=\"favorite\">");
            opt = search.getFavorite();
            SageServlet.printOptionsList(out, SET_CLEAR_OPTS, opt);
            out.println("</select>\r\n" + "</td></tr>\r\n<tr><td>\r\n" + "First Runs and ReRuns:</td><td><select name=\"firstruns\">");
            opt = search.getFirstRuns();
            SageServlet.printOptionsList(out, FIRST_RUNS_OPTS, opt);
            out.println("</select>\r\n" + "</td></tr>\r\n<tr><td>\r\n");
            try {
                SageApi.Api("IsAiringHDTV", new Object[] { new Object() });
                out.println("High Definition:</td><td><select name=\"hdtv\">");
                opt = search.getHDTV();
                SageServlet.printOptionsList(out, SET_CLEAR_OPTS, opt);
                out.println("</select>\r\n" + "</td></tr>\r\n");
            } catch (InvocationTargetException e) {
            }
            out.println("<tr><td>Archived:</td><td><table><tr><td><select name=\"archived\">");
            opt = search.getArchived();
            SageServlet.printOptionsList(out, SET_CLEAR_OPTS, opt);
            out.println("</select>\r\n" + "</td><td>(Ignored for non-TV files)</td></tr></table></td></tr>");
            out.println("<tr><td>Manual Record:</td><td><table><tr><td><select name=\"manrec\">");
            opt = search.getManRec();
            SageServlet.printOptionsList(out, SET_CLEAR_OPTS, opt);
            out.println("</select>\r\n" + "</td><td>(Ignored for non-TV files)</td></tr></table></td></tr>");
            out.println("\r\n<tr><td>Auto-Delete:</td><td><table><tr><td><select name=\"autodelete\">");
            opt = search.getAutodelete();
            SageServlet.printOptionsList(out, SET_CLEAR_OPTS, opt);
            out.println("</select>\r\n" + "</td><td>(Includes Intelligent Recordings and Favorites that have not been archived, marked to manually record, or are currently recording<br/>Ignored for non-TV files.)</td>\r\n" + "</tr></table></td></tr>\r\n" + "<tr><td>Show Partials:</td><td><table><tr><td><select name=\"partials\">");
            opt = search.getPartials();
            SageServlet.printOptionsList(out, PARTIAL_OPTS, opt);
            out.println("</select>\r\n" + "</td><td>(Ignored for non-TV files)</td></tr></table></td></tr>");
            out.println("<tr><td>Primary Sort Criteria:</td><td><select name=\"sort1\">");
            opt = search.getSort1();
            SageServlet.printOptionsList(out, SORT_OPTS, opt);
            out.println("</select>\r\n" + "</td></tr>");
            out.println("<tr><td>Secondary Sort Criteria:</td><td><select name=\"sort2\">");
            opt = search.getSort2();
            SageServlet.printOptionsList(out, SORT_OPTS, opt);
            out.println("</select>\r\n" + "</td></tr>");
            out.println("<tr><td>Group By:</td><td><select name=\"grouping\">");
            opt = req.getParameter("grouping");
            if (opt == null) {
                opt = GetOption(req, "sagetv_recordings_grouping", "None");
            }
            SageServlet.printOptionsList(out, GROUPING_OPTS, opt);
            out.println("</select>\r\n" + "</td></tr>");
            out.println("<tr><td>Results per page:</td><td><select name=\"pagelen\">");
            opt = req.getParameter("pagelen");
            if (opt == null) {
                opt = GetOption(req, "pagelen", Integer.toString(AiringList.DEF_NUM_ITEMS));
            }
            SageServlet.printOptionsList(out, AiringList.NUM_ITEMS_PER_PAGE_OPTS, opt);
            out.println("</select>\r\n" + "</td></tr>");
            out.println("</table>\r\n" + "<p><input type=\"submit\" value=\"Search\"/></p>" + "</form>");
            out.println("</div>");
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
