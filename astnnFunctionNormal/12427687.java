class BackupThread extends Thread {
    public ActionForward createCategory(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            CategoryForm categoryForm = (CategoryForm) form;
            Category category = new Category();
            CategoryService categoryService = Locator.lookupService(CategoryService.class);
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            if (categoryService.getCategoryByCategoryName(categoryForm.getCategoryName()).size() > 0) {
                response.getWriter().write("{success:false,message:'Category name: " + categoryForm.getCategoryName() + " already existed'}");
                return mapping.findForward("");
            }
            if (categoryService.getCategoryByCategoryCode(categoryForm.getCategoryCode()).size() > 0) {
                response.getWriter().write("{success:false,message:'Category code: " + categoryForm.getCategoryCode() + " already existed'}");
                return mapping.findForward("");
            }
            category.setCategoryName(categoryForm.getCategoryName());
            category.setCategoryCode(categoryForm.getCategoryCode());
            category.setCategoryRemark(categoryForm.getCategoryRemark());
            categoryService.insertCategory(category);
            response.getWriter().write("{success:true,message:'New category successfully added'}");
            return mapping.findForward("");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{success:false,message:'Unexpected exception occurred'}");
            return mapping.findForward("");
        }
    }
}
