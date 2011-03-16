#!/bin/bash

function csv() {
  kind=$1
  upper=$(echo $1 | tr '[a-z]' '[A-Z]')
  rm -f $kind.csv
  cat pda.csv | grep $upper > temp
  row=$(cat temp | head -1)
  (echo "$row"; cat temp | grep -v "$row") > $kind.csv
  rm temp
  url
  url=http://pda2.ext.allen-sauer.com/_ah/remote_api
  url=http://localhost:8080/_ah/remote_api
  set -x
  appcfg.py upload_data \
            --url=$url \
            --kind=$upper \
            --config_file=bulkloader.yaml \
            --filename=$kind.csv \
            --email=archer@allen-sauer.com \
            --batch_size=20 \
            --rps_limit=100 \
            --num_threads=20 \
            --http_limit=20 \
            .
  set +x
  ls -l $kind.csv
}

for kind in person contact address calendar
do
  csv $kind
done

rm -f bulkloader-log-*
rm -f bulkloader-error-*
rm -f bulkloader-progress-*
