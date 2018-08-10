# Java Instrumentation Challenge [![Build Status](https://travis-ci.org/mkotb/java-instrumentation-challenge.svg?branch=master)](https://travis-ci.org/mkotb/java-instrumentation-challenge)

This project is a challenge to instrument Spring Boot Requests to get the following information:
- Memory Usage (unique to the request)
- String Creation Count
- Classes loaded
- Time taken to execute the request

### Building the project

You can build the project with `mvn clean install` and you'll find the agent
under the created jar folder.

### Using this project with your Spring Boot Application

You need to specify two parameters in your java command to use this agent with
your spring boot application. Let's assume the agent jar is `mazen-agent.jar`

```-Xbootclasspath/p:mazen-agent.jar -javaagent:mazen-agent.jar```

You have to specify the agent as the boot classpath or else you'll find `NoClassDefFoundError`s
arising from java.lang.String.

... and that's it! You'll see the information above logged to console after requests are made.