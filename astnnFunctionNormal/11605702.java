class BackupThread extends Thread {
        private void mouseDragged(MouseDraggedEvent ev) {
            if (clickPoint == null || selected == 0 || selected == index.length - 1) return;
            Rectangle bounds = canvas.getBounds();
            Point pos = ev.getPoint();
            handlePos[selected].x = pos.x;
            double ind = ((double) pos.x) / (bounds.width - 1.0);
            if (ind < 0.0) ind = 0.0;
            if (ind > 1.0) ind = 1.0;
            index[selected] = 0.001 * ((int) (1000.0 * ind));
            while (index[selected] < index[selected - 1]) {
                double temp = index[selected];
                index[selected] = index[selected - 1];
                index[selected - 1] = temp;
                RGBColor tempcolor = color[selected];
                color[selected] = color[selected - 1];
                color[selected - 1] = tempcolor;
                Point tempPos = handlePos[selected];
                handlePos[selected] = handlePos[selected - 1];
                handlePos[selected - 1] = tempPos;
                selected--;
            }
            while (index[selected] > index[selected + 1]) {
                double temp = index[selected];
                index[selected] = index[selected + 1];
                index[selected + 1] = temp;
                RGBColor tempcolor = color[selected];
                color[selected] = color[selected + 1];
                color[selected + 1] = tempcolor;
                Point tempPos = handlePos[selected];
                handlePos[selected] = handlePos[selected + 1];
                handlePos[selected + 1] = tempPos;
                selected++;
            }
            adjustComponents();
            canvas.repaint();
        }
}
