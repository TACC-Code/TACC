class BackupThread extends Thread {
    public Chats() {
        super();
        final CometdService service = (CometdService) getChannelService();
        final Label chat = new Label("chat");
        chat.setOutputMarkupId(true);
        service.addChannelListener(this, "chat", new IChannelListener() {

            public void onEvent(final String channel, final Map<String, String> map, final IChannelTarget target) {
                target.appendJavascript(JavaScriptUtils.prepend(chat, createChatEntry(map)));
            }
        });
        add(chat);
        final FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        add(feedback);
        add(new CommentsForm("commentForm", feedback));
    }
}
