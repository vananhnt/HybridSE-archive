#!/bin/bash

mkdir 'output'
for f in $(find ./samples/* -type f); do
   echo $f
   start=`date +%s%N`
   file_name="${f##*/}"
   outfile='./output/'$file_name'.out'
   cmd=`java -Xss16m -Xmx40480m -jar corana_api.jar -execute $f M0 > $outfile`
   end=`date +%s%N`
   runtime=$((end-start))
   echo $f $runtime  >> runtime.txt
done

