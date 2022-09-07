class BackupThread extends Thread {
    public static void printReport(PipelineInfo info, PrintStream printStream) {
        List<Result> resultList = info.getResultList();
        Result head = resultList.get(0);
        Result tail = resultList.get(resultList.size() - 1);
        long totalTimestampMilliseconds = tail.getEndTimestamp() - head.getStartTimestamp();
        long totalWaitMilliseconds = head.getWriteWaitTimeMilliseconds();
        printStream.println("Head " + head.getPipeName() + " : time " + ((head.getEndTimestamp() - head.getStartTimestamp())) + ", write wait " + head.getWriteWaitTimeMilliseconds() + " ms (" + getPercent(head.getWriteWaitTimeMilliseconds(), (head.getEndTimestamp() - head.getStartTimestamp())) + ")");
        boolean bottleneckFound = false;
        for (int i = 1; i <= resultList.size() - 2; i++) {
            Result previous = resultList.get(i - 1);
            Result pipe = resultList.get(i);
            Result next = resultList.get(i + 1);
            totalWaitMilliseconds = Math.max(totalWaitMilliseconds, pipe.getReadWaitTimeMilliseconds() + pipe.getWriteWaitTimeMilliseconds());
            boolean suspect = !bottleneckFound && (previous.getWriteWaitTimeMilliseconds() > pipe.getReadWaitTimeMilliseconds() || pipe.getWriteWaitTimeMilliseconds() < next.getReadWaitTimeMilliseconds());
            if (suspect) {
                bottleneckFound = true;
            }
            printStream.println("Pipe " + pipe.getPipeName() + " : time " + ((pipe.getEndTimestamp() - pipe.getStartTimestamp())) + " read wait " + pipe.getReadWaitTimeMilliseconds() + " ms (" + getPercent(pipe.getReadWaitTimeMilliseconds(), (pipe.getEndTimestamp() - pipe.getStartTimestamp())) + "), write wait " + pipe.getWriteWaitTimeMilliseconds() + " ms (" + getPercent(pipe.getWriteWaitTimeMilliseconds(), (pipe.getEndTimestamp() - pipe.getStartTimestamp())) + ")" + (suspect ? " **bottleneck** " : ""));
            previous = pipe;
        }
        totalWaitMilliseconds = Math.max(totalWaitMilliseconds, tail.getWriteWaitTimeMilliseconds());
        printStream.println("Tail " + tail.getPipeName() + " : time " + ((tail.getEndTimestamp() - tail.getStartTimestamp())) + ", read wait " + tail.getReadWaitTimeMilliseconds() + " ms(" + getPercent(tail.getReadWaitTimeMilliseconds(), (tail.getEndTimestamp() - tail.getStartTimestamp())) + ")");
        printStream.println();
        printStream.println("Total time: " + totalTimestampMilliseconds + " ms");
        printStream.println("Total computing time: " + (totalTimestampMilliseconds - totalWaitMilliseconds) + " ms");
        printStream.println("Total wait time: " + totalWaitMilliseconds + " ms");
    }
}
