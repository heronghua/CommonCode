#!/bin/bash
# author : heronghua

#import file
if [ -f ./Api.sh ];then
    . ./Api.sh
fi
if [ -f ./CommmonMethod.sh ];then
    . ./CommmonMethod.sh
fi


echo "request url is $GET_USER_INFO"
curlPostWithHeaderFile body header $GET_USER_INFO
echo "response:"
echo
echo $postResult
