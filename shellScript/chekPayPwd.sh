#!/bin/bash
# author : heronghua

#import file
if [ -f ./Api.sh ];then
    . ./Api.sh
fi
if [ -f ./CommmonMethod.sh ];then
    . ./CommmonMethod.sh
fi


echo "request url is $CHECK_PAY_PWD" 
curlPostWithHeaderFile "\"{\\\"paypwd\\\":\\\"123456\\\"}\"" header $CHECK_PAY_PWD
echo "response:"
echo
echo $postResult
