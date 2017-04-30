#!/bin/bash 
SOURCE_FOLDER=$1
LIB_PATH=$2
ASPECT_FOLDER=$3
ASPECT_FILE=ParseSequence.aj

for i in 'aspectjtools.jar' 'aspectjrt.jar' 'plantuml.jar'
do
    CLASSPATH=$CLASSPATH:$LIB_PATH/$i
done


CLASSES_DIR=$SOURCE_FOLDER/compile-time 

rm -rf $CLASSES_DIR
mkdir $CLASSES_DIR

cp $ASPECT_FOLDER/$ASPECT_FILE $SOURCE_FOLDER

# Compile the sources
echo "Compiling..."

java -cp $CLASSPATH org.aspectj.tools.ajc.Main -source 1.5 -d $CLASSES_DIR $SOURCE_FOLDER/*.java $SOURCE_FOLDER/$ASPECT_FILE

# Run the example and check that aspect logic is applied
echo "Running the sample..."
java -cp $CLASSPATH:$CLASSES_DIR Main