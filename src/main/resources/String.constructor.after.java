com.mazenk.instrumentation.InstrumentationData data = com.mazenk.instrumentation.DataCollector.getCurrentData();

if (data != null) {
    data.incrementStringCount();
}