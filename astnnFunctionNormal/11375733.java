class BackupThread extends Thread {
    public void dragEnd(int x, int y) {
        ScrEvent aEvent;
        ScrEvent newEvent;
        ExplodeGraphicContext egc = (ExplodeGraphicContext) gc;
        int deltaY = y - startingPoint.y;
        int deltaX = x - startingPoint.x;
        if (itsMoveMode == CLONE) {
            ((UndoableData) egc.getDataModel()).beginUpdate();
            for (Enumeration e = egc.getSelection().getSelected(); e.hasMoreElements(); ) {
                aEvent = (ScrEvent) e.nextElement();
                newEvent = new ScrEvent(aEvent.getDataModel(), aEvent.getTime(), aEvent.getPitch(), aEvent.getVelocity(), aEvent.getDuration(), aEvent.getChannel());
                aEvent.getDataModel().addEvent(newEvent);
                egc.getAdapter().setX(newEvent, egc.getAdapter().getX(aEvent) + deltaX);
                egc.getAdapter().setY(newEvent, egc.getAdapter().getY(aEvent) + deltaY);
            }
            ((UndoableData) egc.getDataModel()).endUpdate();
        } else {
            ((UndoableData) egc.getDataModel()).beginUpdate();
            for (Enumeration e = egc.getSelection().getSelected(); e.hasMoreElements(); ) {
                aEvent = (ScrEvent) e.nextElement();
                if (deltaX != 0) egc.getAdapter().setX(aEvent, egc.getAdapter().getX(aEvent) + deltaX);
                if (deltaY != 0) egc.getAdapter().setY(aEvent, egc.getAdapter().getY(aEvent) + deltaY);
            }
            ((UndoableData) egc.getDataModel()).endUpdate();
        }
        mountIModule(itsSelecter);
        gc.getGraphicDestination().repaint();
    }
}
