#!/bin/bash
# author : heronghua

getJsonValueRtv=""
function getJsonValue()
{
  local json=$1
  local key=$2

  if [[ -z "$3" ]]; then
    local num=1
  else
    local num=$3
  fi

  getJsonValueRtv=$(echo "${json}" | awk -F"[,:}]" '{for(i=1;i<=NF;i++){if($i~/'${key}'\042/){print $(i+1)}}}' | tr -d '"' | sed -n ${num}p)

}

postResult=""
function curlPostWithHeaderFile()
{
	local body=$1
	local header=$2
	local url=$3
	local extra=$4

	commond="curl -X POST -d $body"
	while read line
	do
		commond=$commond" -H \"$line\""
	done<$header
	
	#add extra header
	if [ ! -n "$extra" ];then
		commond=$commond" $extra"
	else
		commond=$commond
	fi

	commond=$commond" $url"


	echo $commond
	postResult=`echo $commond|bash`

}
