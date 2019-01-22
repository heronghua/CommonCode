#!/bin/bash
# authhor : heronghua
# this is used for constant value 

DOMAIN="http://api.ydk365.com"
#DOMAIN="https://kuang.4kb.cn"

API=$DOMAIN"/api"

LOGIN=$API"/user/app_login"
GET_USER_INFO=$API"/user/getUserInfo"
GET_K_LINE=$API"/symbol/getKline"
CHECK_PAY_PWD=$API"/user/checkPayPwd"
