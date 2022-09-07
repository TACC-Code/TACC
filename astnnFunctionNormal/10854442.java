class BackupThread extends Thread {
    public ActionForward createUsage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            UsageForm usageForm = (UsageForm) form;
            Usage usage = new Usage();
            UsageService usageService = Locator.lookupService(UsageService.class);
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            if (usageService.getUsageByUsageName(usageForm.getUsageName()).size() > 0) {
                response.getWriter().write("{success:false,message:'Usage name: " + usageForm.getUsageName() + " already existed'}");
                return mapping.findForward("");
            }
            if (usageService.getUsageByUsageCode(usageForm.getUsageCode()).size() > 0) {
                response.getWriter().write("{success:false,message:'Usage code: " + usageForm.getUsageCode() + " already existed'}");
                return mapping.findForward("");
            }
            usage.setUsageName(usageForm.getUsageName());
            usage.setUsageCode(usageForm.getUsageCode());
            usage.setUsageRemark(usageForm.getUsageRemark());
            usageService.insertUsage(usage);
            response.getWriter().write("{success:true,message:'New usage successfully added'}");
            return mapping.findForward("");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{success:false,message:'Unexpected exception occurred'}");
            return mapping.findForward("");
        }
    }
}
