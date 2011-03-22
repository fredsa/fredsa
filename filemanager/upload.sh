#!/bin/bash
#
set -e

GET_UPLOAD_URL="/get-upload-url"

if [ $# -lt 2 ]
then
  echo "App Engine asset uploader."
  echo ""
	echo "Usage:"
	echo "  $0 <url> <file1> [file2] ..."
	exit 1
fi

url=$1
shift

while [ $# -gt 0 ]
do
	file=$1
	shift
	echo "FILE: $file"

	# determine MIME Type
	mime_type=$(file --brief --mime-type $file)
	echo "- MIME Type: $mime_type"

	# request blobstore upload URL
	upload_url=$(curl -s $url$GET_UPLOAD_URL)

	# fix for devappserver lacking scheme/host/port
	upload_url=${url}${upload_url#${url}}

	echo "- Upload URL: $upload_url"

	# upload content
	curl -F "file=@$file;filename=$file;type=$mime_type" $upload_url
done
