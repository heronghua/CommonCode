#!/bin/bash
# author : heronghua

a=$1
r=$2
g=$3
b=$4

result=""

result=`echo "obase=16;$a"|bc`
result=$result`echo "obase=16;$r"|bc`
result=$result`echo "obase=16;$g"|bc`
result=$result`echo "obase=16;$b"|bc`

echo "The result is :"
echo $result
