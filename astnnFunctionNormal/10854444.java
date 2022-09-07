class BackupThread extends Thread {
    public ActionForward deleteUsage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            UsageForm usageForm = (UsageForm) form;
            UsageService usageService = Locator.lookupService(UsageService.class);
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            Usage usage = usageService.getUsageByUsageId(usageForm.getUsageId(), true);
            if (usage == null) {
                response.getWriter().write("{success:true,message:'This usage information has already been deleted'}");
                return mapping.findForward("");
            }
            if (usage.getDocuments().size() != 0) {
                response.getWriter().write("{success:true,message:'This usage information has been attached to some document numbers, it can not be deleted'}");
                return mapping.findForward("");
            }
            usageService.deleteUsage(usageForm.getUsageId());
            response.getWriter().write("{success:true,message:'Successfully delete usage information'}");
            return mapping.findForward("");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{success:false,message:'Unexpected exception occurred'}");
            return mapping.findForward("");
        }
    }
}
