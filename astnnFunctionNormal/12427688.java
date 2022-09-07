class BackupThread extends Thread {
    public ActionForward updateCategory(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
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
            List<Category> categoryList = categoryService.getCategoryByCategoryName(categoryForm.getCategoryName());
            if (categoryList.size() > 0 && categoryList.get(0).getCategoryId() != categoryForm.getCategoryId()) {
                response.getWriter().write("{success:false,message:'Category name: " + categoryForm.getCategoryName() + " already existed'}");
                return mapping.findForward("");
            }
            categoryList = categoryService.getCategoryByCategoryCode(categoryForm.getCategoryCode());
            if (categoryList.size() > 0 && categoryList.get(0).getCategoryId() != categoryForm.getCategoryId()) {
                response.getWriter().write("{success:false,message:'Category code: " + categoryForm.getCategoryCode() + " already existed'}");
                return mapping.findForward("");
            }
            category.setCategoryName(categoryForm.getCategoryName());
            category.setCategoryCode(categoryForm.getCategoryCode());
            category.setCategoryRemark(categoryForm.getCategoryRemark());
            categoryService.updateCategory(category);
            response.getWriter().write("{success:true,message:'Modify category information successfully'}");
            return mapping.findForward("");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{success:false,message:'Unexpected exception occurred'}");
            return mapping.findForward("");
        }
    }
}
