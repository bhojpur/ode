#!/bin/bash

# Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
# THE SOFTWARE.

# Script to create the training material using the CLI

set -e
set -u
set -x

now=$(date +"%s")

HOSTNAME=${HOSTNAME:-localhost}
PORT=${PORT:-4064}
ROOT_PASSWORD=${ROOT_PASSWORD:-ode}
GROUP_NAME=${GROUP_NAME:-robot_group}-$now
GROUP_PERMS=${GROUP_PERMS:-rwra--}
GROUP_NAME_2=${GROUP_NAME_2:-robot_group_2}-$now
USER_NAME=${USER_NAME:-robot_user}-$now
USER_PASSWORD=${USER_PASSWORD:-ome}
CONFIG_FILENAME=${CONFIG_FILENAME:-robot_ice.config}
IMAGE_NAME=${IMAGE_NAME:-test&acquisitionDate=2012-01-01_00-00-00&sizeZ=3&sizeT=10.fake}
MULTI_C_IMAGE_NAME=${MULTI_C_IMAGE_NAME:-test&acquisitionDate=2012-01-01_00-00-00&sizeC=3&sizeZ=3&sizeT=10.fake}
TINY_IMAGE_NAME=${TINY_IMAGE_NAME:-test&acquisitionDate=2012-01-01_00-00-00.fake}
MIF_IMAGE_NAME=${MIF_IMAGE_NAME:-test&series=3.fake}
BIG_IMAGE_NAME=${BIG_IMAGE_NAME:-test&sizeX=4000&sizeY=4000.fake}
PLATE_NAME=${PLATE_NAME:-test&plates=1&plateAcqs=2&plateRows=2&plateCols=3&fields=5&screens=0.fake}
TINY_PLATE_NAME=${TINY_PLATE_NAME:-test&plates=1&plateAcqs=1&plateRows=1&plateCols=1&fields=1&screens=0.fake}
BULK_ANNOTATION_CSV=${BULK_ANNOTATION_CSV:-bulk_annotation.csv}
FILE_ANNOTATION=${FILE_ANNOTATION:-robot_file_annotation.txt}
FILE_ANNOTATION2=${FILE_ANNOTATION2:-robot_file_annotation2.txt}

# Create robot user and group
bin/ode login root@$HOSTNAME:$PORT -w $ROOT_PASSWORD
bin/ode group add $GROUP_NAME --ignore-existing --perms $GROUP_PERMS
bin/ode group add $GROUP_NAME_2 --ignore-existing
bin/ode user add $USER_NAME $USER_NAME $USER_NAME $GROUP_NAME $GROUP_NAME_2 --ignore-existing -P $USER_PASSWORD
bin/ode user joingroup --name $USER_NAME --group-name $GROUP_NAME --as-owner
bin/ode logout

# Create fake files
touch $IMAGE_NAME
touch $MULTI_C_IMAGE_NAME
touch $TINY_IMAGE_NAME
touch $PLATE_NAME
touch $TINY_PLATE_NAME
touch $MIF_IMAGE_NAME
touch $BIG_IMAGE_NAME

# Python script for setting posX and posY on wellsamples
# Used below after importing a plate
WELLSCRIPT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/resources/well_sample_posXY.py"

# Create batch annotation csv
echo "Well,Well Type,Concentration" > "$BULK_ANNOTATION_CSV"
echo "A1,Control,0" >> "$BULK_ANNOTATION_CSV"
echo "A2,Treatment,10" >> "$BULK_ANNOTATION_CSV"

# Create files for upload as File Annotation
echo "Robot test file annotations" > "$FILE_ANNOTATION"
echo "Another test file annotation" > "$FILE_ANNOTATION2"

# Create robot setup
bin/ode login $USER_NAME@$HOSTNAME:$PORT -w $USER_PASSWORD
# Parse the sessions file to get session key
key=$(bin/ode sessions key)
echo "Session key: $key"
nProjects=1
nDatasets=1
nImages=10
echo "Creating projects and datasets"
for (( i=1; i<=$nProjects; i++ ))
do
  project=$(bin/ode obj new Project name='Project '$i)
  for (( j=1; j<=$nDatasets; j++ ))
  do
    dataset=$(bin/ode obj new Dataset name='Dataset '$i-$j)
    bin/ode obj new ProjectDatasetLink parent=$project child=$dataset
    echo "Importing images into dataset"
    for (( k=1; k<=$nImages; k++ ))
    do
      bin/ode import -d $dataset $IMAGE_NAME --debug ERROR > show_import.log 2>&1
      imageid=$(sed -n -e 's/^Image://p' show_import.log)
      bin/ode obj update Image:$imageid name=test_view$k
    done
  done
done

# Create Dataset with images for deleting
delDs=$(bin/ode obj new Dataset name='Delete')
for (( k=1; k<=10; k++ ))
do
  bin/ode import -d $delDs $TINY_IMAGE_NAME --debug ERROR
done

# Create Dataset with MIF images
mifDs=$(bin/ode obj new Dataset name='MIF Images')
for (( k=1; k<=2; k++ ))
do
  bin/ode import -d $mifDs $MIF_IMAGE_NAME --debug ERROR
done

# Create Dataset with multi channel images
mcDs=$(bin/ode obj new Dataset name='MultiChannel Images')
for (( k=1; k<=2; k++ ))
do
  bin/ode import -d $mcDs $MULTI_C_IMAGE_NAME --debug ERROR
done

# Create Dataset with Big Image
bigDs=$(bin/ode obj new Dataset name='Big Images')
bin/ode import -d $bigDs $BIG_IMAGE_NAME --debug ERROR

# Import Plate and rename
bin/ode import $PLATE_NAME --debug ERROR > plate_import.log 2>&1
plateid=$(sed -n -e 's/^Plate://p' plate_import.log)
bin/ode obj update Plate:$plateid name=spwTests
# Use populate_metadata to upload and attach bulk annotation csv
# We use testtables to only try populate if tables are working
export ODE_DEV_PLUGINS=1      # required to enable 'metadata' CLI
bin/ode metadata testtables && bin/ode metadata populate Plate:$plateid --file $BULK_ANNOTATION_CSV

# Run script to populate WellSamples with posX and posY values
PYTHONPATH=./lib/python python $WELLSCRIPT $HOSTNAME $PORT $key $plateid

# Import Tiny Plate (single acquisition & well) and rename
bin/ode import $TINY_PLATE_NAME --debug ERROR > show_import.log 2>&1
plateid=$(sed -n -e 's/^Plate://p' show_import.log)
bin/ode obj update Plate:$plateid name=tinyPlate
# Import Image into Project/Dataset and rename for test
showP=$(bin/ode obj new Project name='showProject')
showD=$(bin/ode obj new Dataset name='showDataset')
bin/ode obj new ProjectDatasetLink parent=$showP child=$showD
for (( k=1; k<=2; k++ ))
do
  bin/ode import -d $showD $IMAGE_NAME --debug ERROR > show_import.log 2>&1
  imageid=$(sed -n -e 's/^Image://p' show_import.log)
  bin/ode obj update Image:$imageid name=testShowImage$k
done

# Create Screen with empty plates for Create Scenario
scrDs=$(bin/ode obj new Screen name='CreateScenario')
for (( k=1; k<=6; k++ ))
do
  bin/ode import -r $scrDs $TINY_PLATE_NAME --debug ERROR
done

# Create Orphaned Images for Create Scenario
for (( k=1; k<=10; k++ ))
do
  bin/ode import $TINY_IMAGE_NAME --debug ERROR
done

# Upload files and create FileAnnotations
ofile=$(bin/ode upload $FILE_ANNOTATION)
bin/ode obj new FileAnnotation file=$ofile
ofile2=$(bin/ode upload $FILE_ANNOTATION2)
bin/ode obj new FileAnnotation file=$ofile2

# Logout
bin/ode logout

# Create ice.config file
echo "ode.host=$HOSTNAME" > "$CONFIG_FILENAME"
echo "ode.port=$PORT" >> "$CONFIG_FILENAME"
echo "ode.user=$USER_NAME" >> "$CONFIG_FILENAME"
echo "ode.pass=$USER_PASSWORD" >> "$CONFIG_FILENAME"
echo "ode.projectid=${project##*:}" >> "$CONFIG_FILENAME"
echo "ode.datasetid=${dataset##*:}" >> "$CONFIG_FILENAME"

# Remove fake file and logs
rm *.fake
rm plate_import.log
rm show_import.log
