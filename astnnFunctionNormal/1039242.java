class BackupThread extends Thread {
    protected void paintTrack(Graphics2D g2D) {
        if (selected) g2D.setColor(Color.red); else g2D.setColor(Color.blue);
        g2D.fillOval(xPos, yPos, size, size);
        g2D.setColor(trackColor);
        g2D.fillOval(xPos + border, yPos + border, size - border * 2, size - border * 2);
        g2D.setColor(Color.black);
        int nameLength = midiTrack.getName().length();
        if (nameLength > 12) {
            g2D.setFont(smallTrackFont);
            g2D.drawString(midiTrack.getName(), xPos + size / 2 - nameLength / 2 * smallLetterWidth, yPos + size / 2 + nameYOffset);
        } else {
            g2D.setFont(trackFont);
            g2D.drawString(midiTrack.getName(), xPos + size / 2 - nameLength / 2 * letterWidth, yPos + size / 2 + nameYOffset);
        }
        g2D.setFont(trackFont);
        if (midiTrack.muted()) g2D.setColor(MASLookAndFeel.getTrackMuteOnColor()); else g2D.setColor(MASLookAndFeel.getTrackMuteOffColor());
        g2D.fillRect(muteRect.x, muteRect.y, muteRect.width, muteRect.height);
        g2D.setColor(Color.white);
        g2D.drawString("M", muteRect.x + buttonLetterOffset, muteRect.y + muteRect.height - buttonLetterOffset);
        if (midiTrack.soloed()) g2D.setColor(MASLookAndFeel.getTrackSoloOnColor()); else g2D.setColor(MASLookAndFeel.getTrackSoloOffColor());
        g2D.fillRect(soloRect.x, soloRect.y, soloRect.width, soloRect.height);
        g2D.setColor(Color.white);
        g2D.drawString("S", soloRect.x + buttonLetterOffset, soloRect.y + soloRect.height - buttonLetterOffset);
        g2D.setColor(MASLookAndFeel.getTrackAgentVisibleColor());
        g2D.fillRect(agVisRect.x, agVisRect.y, agVisRect.width, agVisRect.height);
        g2D.setColor(Color.white);
        g2D.drawString("A", agVisRect.x + buttonLetterOffset, agVisRect.y + agVisRect.height - buttonLetterOffset);
        if (!agentsVisible) {
            g2D.setColor(Color.red);
            g2D.drawLine(agVisRect.x, agVisRect.y, agVisRect.x + agVisRect.width, agVisRect.y + agVisRect.height);
            g2D.drawLine(agVisRect.x + agVisRect.width, agVisRect.y, agVisRect.x, agVisRect.y + agVisRect.height);
        }
        g2D.setColor(MASLookAndFeel.getTrackConnectionVisibleColor());
        g2D.fillRect(connVisRect.x, connVisRect.y, connVisRect.width, connVisRect.height);
        g2D.setColor(Color.white);
        g2D.drawString("C", connVisRect.x + buttonLetterOffset, connVisRect.y + connVisRect.height - buttonLetterOffset);
        if (!connectionsVisible) {
            g2D.setColor(Color.red);
            g2D.drawLine(connVisRect.x, connVisRect.y, connVisRect.x + connVisRect.width, connVisRect.y + connVisRect.height);
            g2D.drawLine(connVisRect.x + connVisRect.width, connVisRect.y, connVisRect.x, connVisRect.y + connVisRect.height);
        }
        g2D.setColor(Color.black);
        int midiChannel = midiTrack.getChannel() + 1;
        if (midiChannel < 10) g2D.drawString(Integer.toString(midiChannel), xPos + size / 2 - 3, yPos + size / 2 + channelYOffset); else g2D.drawString(Integer.toString(midiChannel), xPos + size / 2 - 5, yPos + size / 2 + channelYOffset);
    }
}
