#!/bin/bash
# author : heronghua

#import file
if [ -f ./Api.sh ];then
    . ./Api.sh
fi
if [ -f ./CommmonMethod.sh ];then
    . ./CommmonMethod.sh
fi

#initial header

echo "Content-Type:application/json">header
echo "request url is $LOGIN"
loginRtv=`curl -H $(cat header) -X POST -d @body.json "$LOGIN"`
echo "response:"
echo $loginRtv
echo 

#write token to header file
getJsonValue $loginRtv token
token=$getJsonValueRtv
getJsonValue $loginRtv user_id
userId=$getJsonValueRtv
echo  "Content-Type:application/json">header
echo  "x-access-user-token:${token}">>header
echo  "x-access-user-id:${userId}">>header
