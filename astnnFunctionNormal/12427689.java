class BackupThread extends Thread {
    public ActionForward deleteCategory(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            CategoryForm categoryForm = (CategoryForm) form;
            CategoryService categoryService = Locator.lookupService(CategoryService.class);
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            Category category = categoryService.getCategoryByCategoryId(categoryForm.getCategoryId(), true);
            if (category == null) {
                response.getWriter().write("{success:true,message:'This category information has already been deleted'}");
                return mapping.findForward("");
            }
            if (category.getDocuments().size() != 0) {
                response.getWriter().write("{success:true,message:'This category information has been attached to some document numbers, it can not be deleted'}");
                return mapping.findForward("");
            }
            categoryService.deleteCategory(categoryForm.getCategoryId());
            response.getWriter().write("{success:true,message:'Successfully delete category information'}");
            return mapping.findForward("");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{success:false,message:'Unexpected exception occurred'}");
            return mapping.findForward("");
        }
    }
}
