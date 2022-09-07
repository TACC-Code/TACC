class BackupThread extends Thread {
    public void mousePressed(MouseEvent e) {
        Debug.println(5, "mouse pressed: start zoom functionality");
        mouseDown = true;
        mouseZoomXPress = e.getPoint().x;
        mouseZoomYPress = e.getPoint().y;
        mouseZoomXReleas = mouseZoomXPress;
        mouseZoomYReleas = mouseZoomYPress;
        mouseZoomXOld = mouseZoomXPress;
        mouseZoomYOld = mouseZoomYPress;
        shiftActive = GToolkit.isShiftKey(e);
        ctrlActive = GToolkit.isCtrlKey(e);
        ALayer l = getFocussedClip().getSelectedLayer();
        int i = l.getPlotter().getInsideChannelIndex(e.getPoint());
        mouseChannelPlotter = l.getChannel(i).getPlotter();
    }
}
