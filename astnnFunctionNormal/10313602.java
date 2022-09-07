class BackupThread extends Thread {
    private void publish(Comment comment) {
        final ChannelEvent event = new ChannelEvent("chat");
        event.addData("author", comment.getAuthor().getForename() + " " + comment.getAuthor().getSurname());
        event.addData("message", comment.getMessage());
        PrettyTime prettyTime = new PrettyTime();
        event.addData("time", prettyTime.format(comment.getCommentDate()));
        getChannelService().publish(event);
    }
}
