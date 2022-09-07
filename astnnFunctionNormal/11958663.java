class BackupThread extends Thread {
    @Override
    public void perform(WebContext context) {
        Channel channel = context.getChannel();
        boolean newChannel = (channel == null);
        if (channel == null) channel = context.newChannel();
        createPage(context);
        setTitle(Text.get(context.getLocale(), "Home"));
        if (newChannel) {
            Formatter f = new Formatter();
            f.format("document.observe(\"dom:loaded\", function() {%n");
            f.format("	new Ajax.Request(\"/j\", {%n");
            f.format("		requestHeaders: {%n");
            f.format("			\"%s\": \"%s\"%n", WebContext.X_ANNONE_CHANNEL_ID, channel.getId());
            f.format("		},%n");
            f.format("		parameters: {%n");
            f.format("			newChannel: \"true\"%n");
            f.format("		},%n");
            f.format("		onFailure: function(response) {%n");
            f.format("			alert(\"%s\\n  (\" + response.status + \") \" + response.statusText);%n", Text.get(context.getLocale(), "Can''t start new session."));
            f.format("		}%n");
            f.format("	});%n");
            f.format("});%n");
            SCRIPT script = new SCRIPT(new Plain(f.toString()));
            head.add(script);
        }
        content.add(new NOSCRIPT());
        updateResponse(context, 200);
    }
}
