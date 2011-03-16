#!/bin/bash

function csv() {
  kind=$1
  upper=$(echo $1 | tr '[a-z]' '[A-Z]')
  rm -f $kind.csv
  cat pda.csv | grep $upper > temp
  row=$(cat temp | head -1)
  (echo "$row"; cat temp | grep -v "$row") > $kind.csv
  rm temp
  appcfg.py upload_data \
            --url=http://pda2.ext.allen-sauer.com/_ah/remote_api \
            --kind=$upper \
            --config_file=bulkloader.yaml --file=$kind.csv \
            --email=archer@allen-sauer.com \
            --batch_size=50 \
            --rps_limit=100 \
            --num_threads=20 \
            .
  ls -l $kind.csv
}

for kind in person contact address calendar
do
  csv $kind
done

rm -f bulkloader-log-*
rm -f bulkloader-error-*
rm -f bulkloader-progress-*
