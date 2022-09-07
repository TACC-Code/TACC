class BackupThread extends Thread {
    @SuppressWarnings("unchecked")
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Returning REPLICATE view");
        RequestParameters rp = new RequestParameters();
        rp.parse(request);
        Map myModel = new HashMap();
        myModel.put("now", (new java.util.Date()).toString());
        String view = "";
        if (request.getParameter("filterValue") == null) {
            if (rp.getFilterNames()[0].equals(MemberFilter.FILTER_CHANNEL_BY_NAME)) {
                myModel.put("list", channelManager.getChannelList(true));
            } else {
                myModel.put("list", functionManager.getFunctionList(true));
            }
            view = "replicate-list";
        } else {
            ArrayList<Member> memberListWithDetails = getMemberManager().getMemberListWithDetails(rp.getFilterNames(), rp.getFilterValues(), MemberPermissions.USER_ID_ADMIN_INTERN);
            if (rp.getFormat().equals(FORMAT_BIRTHDAYS)) {
                view = "replicate-birthdays";
                Collections.sort(memberListWithDetails, new BirthdayComparator(true));
                myModel.put("members", memberListWithDetails);
            } else if (rp.getFormat().equals(FORMAT_QMAIL)) {
                view = "replicate-qmail";
                myModel.put("members", memberListWithDetails);
            } else {
                view = "replicate-postfix";
                myModel.put("members", memberListWithDetails);
            }
        }
        return new ModelAndView(view, "model", myModel);
    }
}
