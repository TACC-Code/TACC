class BackupThread extends Thread {
            @Override
            public void mouseClicked(final MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                if (!layouter.isOutside(x, y)) {
                    System.out.println("Set start address " + layouter.getChannel(x, y));
                }
            }
}
