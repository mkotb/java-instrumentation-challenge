// accessible variables:
// HttpServletRequest request
// HttpServletResponse response
{
    com.mazenk.instrumentation.InstrumentationData data = new com.mazenk.instrumentation.InstrumentationData();
    data.additionalMetadata().put("path", request.getPathTranslated());
    com.mazenk.instrumentation.DataCollector.beginInstrumentation(data);
}