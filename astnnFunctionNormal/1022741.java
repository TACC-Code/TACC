class BackupThread extends Thread {
    public ServiceTopic(Object topicData, String name, DomainParticipant participant, Publisher publisher, Subscriber subscriber, QoSParameters readerQos, QoSParameters writerQos) throws ClassNotFoundException, InstantiationException, IllegalAccessException, ImpossibleToCreateDDSTopic {
        this(topicData, name, participant, publisher, subscriber, null, readerQos, writerQos);
    }
}
