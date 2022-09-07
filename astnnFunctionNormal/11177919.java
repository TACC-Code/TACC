class BackupThread extends Thread {
    public void addItem(double weight, Object item) {
        if (weight >= weights[weights.length - 1]) {
            return;
        }
        int index = items.length - 1;
        items[index] = item;
        weights[index] = weight;
        index--;
        while (index >= 0) {
            if (weights[index] > weights[index + 1]) {
                Object tmpO = items[index];
                double tmpW = weights[index];
                items[index] = items[index + 1];
                weights[index] = weights[index + 1];
                items[index + 1] = tmpO;
                weights[index + 1] = tmpW;
                index--;
            } else {
                break;
            }
        }
        itemsInQueue = itemsInQueue < items.length ? itemsInQueue + 1 : items.length;
    }
}
