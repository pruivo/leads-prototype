#!/bin/sh

WORK_DIR=`cd $(dirname $0); pwd`
CLASS=org.jboss.pruivo.leads.InfinispanServerMain

CP=${PWD}

for jar in ${WORK_DIR}/../target/lib/*.jar; do
    CP=${CP}:${jar};
done

${JAVA_HOME}/bin/java -cp ${CP} ${CLASS} $*

exit 0;
