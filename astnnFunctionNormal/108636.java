class BackupThread extends Thread {
    public void writeXML() {
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(stats));
            output.write("<?xml version=\"1.0\"?>\n");
            output.write("<stats>\n");
            java.util.Date now = new java.util.Date();
            DateFormat formatter = new SimpleDateFormat("EEE dd/MM/yyyy HH:mm:ss");
            output.write("<date>" + formatter.format(now) + "</date>\n");
            output.write("<users>\n");
            output.write("<now>" + dbc.getUserCount() + "</now>\n");
            output.write("<max>" + dbc.getMaxUserCount() + "</max>\n");
            output.write("</users>\n");
            output.write("<opers>\n");
            output.write("<now>" + dbc.getOperCount() + "</now>\n");
            output.write("<max>" + dbc.getMaxOperCount() + "</max>\n");
            output.write("</opers>\n");
            output.write("<servers>\n");
            output.write("<now>" + dbc.getServerCount() + "</now>\n");
            output.write("<max>" + dbc.getMaxServerCount() + "</max>\n");
            String[][] servers = dbc.getServerTable();
            for (int i = 0; i < servers.length; i++) {
                output.write("<server users=\"" + servers[i][1] + "\" opers=\"" + servers[i][2] + "\">" + servers[i][0] + "</server>\n");
            }
            output.write("</servers>\n");
            output.write("<channels>\n");
            output.write("<now>" + dbc.getChannelCount() + "</now>\n");
            output.write("<max>" + dbc.getMaxChannelCount() + "</max>\n");
            String[][] channels = dbc.getChannelTable();
            for (int i = 0; i < channels.length; i++) {
                output.write("<channel users=\"" + channels[i][1] + "\">" + channels[i][0] + "</channel>\n");
            }
            output.write("</channels>\n");
            output.write("</stats>\n");
            output.close();
        } catch (Exception e) {
            C.printDebug("Error writing stats XML.");
        }
    }
}
