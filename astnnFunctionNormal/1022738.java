class BackupThread extends Thread {
    public ServiceTopic(Object topicData, String name, DomainParticipant participant, Publisher publisher, Subscriber subscriber, QoSParameters topicQos, QoSParameters readerQos, QoSParameters writerQos) throws ClassNotFoundException, InstantiationException, IllegalAccessException, ImpossibleToCreateDDSTopic {
        this.topicDataType = topicData;
        this.topicID = name;
        this.publisher = publisher;
        this.subscriber = subscriber;
        this.participant = participant;
        this.topicTypeSupportClassName = this.topicDataType.getClass().getName() + "TypeSupport";
        this.topicSeqHolderClassName = this.topicDataType.getClass().getName() + "SeqHolder";
        this.topicDataWriterClassName = this.topicDataType.getClass().getName() + "DataWriter";
        this.topicDataReaderClassName = this.topicDataType.getClass().getName() + "DataReader";
        Class tsClassDefinition = Class.forName(this.topicTypeSupportClassName);
        this.topicTS = (TypeSupport) tsClassDefinition.newInstance();
        ErrorHandler.checkHandle(topicTS, "new TypeSupport");
        this.topicTypeNameTS = this.topicTS.get_type_name();
        this.status = this.topicTS.register_type(this.participant, this.topicTypeNameTS);
        ErrorHandler.checkStatus(this.status, "ServiceDDS.ServiceTopic.TypeSupport.register_type");
        this.status = this.participant.get_default_topic_qos(this.defaultTopicQos);
        ErrorHandler.checkStatus(status, "ServiceDDS: getting topic qos for topic " + this.topicDataType);
        if (topicQos != null) {
            if (topicQos.deadline != null) {
                this.defaultTopicQos.value.deadline = new DDS.DeadlineQosPolicy(topicQos.deadline);
            }
            if (topicQos.history != null) {
                this.defaultTopicQos.value.history = new DDS.HistoryQosPolicy(topicQos.history, topicQos.keep);
            }
        }
        this.topic = this.participant.create_topic(this.topicID, this.topicTypeNameTS, this.defaultTopicQos.value, null, ANY_STATUS.value);
        if (this.topic == null) throw new ImpossibleToCreateDDSTopic(topicTypeNameTS);
    }
}
