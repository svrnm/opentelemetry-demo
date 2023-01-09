#!/bin/sh
if [ "${OTEL_INSTRUMENTATION_DISABLED}" = true ] ; then  
    # Simply reset JAVA_TOOL_OPTIONS which was set to contain the javaagent in 
    # the Dockerfile
    JAVA_TOOL_OPTIONS=""
fi
./build/install/hipstershop/bin/AdService