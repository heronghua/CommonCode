#!/bin/bash
# author : heronghua

#import file
if [ -f ./Api.sh ];then
    . ./Api.sh
fi
if [ -f ./CommmonMethod.sh ];then
    . ./CommmonMethod.sh
fi


echo "request url is $GET_K_LINE"
curlPostWithHeaderFile "\"{\\\"symbol\\\":\\\"btc\\\",\\\"period\\\":\\\"1min\\\"}\"" header $GET_K_LINE
echo "response:"
echo
echo $postResult
echo $postResult>result
