class BackupThread extends Thread {
    protected void initialize(final IModel<Commentable> model, String username, boolean readVisible, boolean writeVisible, IDataProvider<AbstractComment> provider) {
        add(CSSPackageResource.getHeaderContribution(new ResourceReference(CommentPanel.class, "comment.css")));
        WebMarkupContainer wmc = new WebMarkupContainer("commentContainer");
        add(wmc);
        CompoundPropertyModel<AbstractComment> commentModel = createDefaultComment(model.getObject());
        commentModel.getObject().setName(username);
        final CurrentCommentListPanel comments = new CurrentCommentListPanel("cclp", model, null, provider, pageSize);
        comments.setVisible(readVisible);
        comments.setDateFormat(dateFormat);
        comments.setLocale(locale);
        comments.setProviderAll(providerAll);
        comments.init();
        add(comments.setOutputMarkupId(true));
        final CommentForm commentForm = new CommentForm("commentForm", commentModel) {

            private static final long serialVersionUID = 1L;

            @Override
            CompoundPropertyModel<AbstractComment> saveComment(AbstractComment comment) {
                executeSaveComment(comment);
                return createDefaultComment(model.getObject());
            }

            @Override
            CurrentCommentListPanel getComments() {
                return comments;
            }
        };
        processValidators(commentForm);
        commentForm.setWebsiteVisible(websiteVisible);
        commentForm.setEmailVisible(emailVisible);
        commentForm.setVisible(writeVisible);
        commentForm.setAnonymous(anonymous);
        commentForm.setSendEmail(sendEmail);
        commentForm.setUseCaptcha(useCaptcha);
        commentForm.setOptions(options);
        commentForm.init();
        wmc.add(commentForm);
    }
}
