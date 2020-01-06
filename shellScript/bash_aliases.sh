#!/bin/bash
# This file can be imported by these code in .bashrc file at $home
#
#if [ -f .bash_aliases ];then
#	. .bash_aliases
#fi


###################################################################### alias start ############################################################################################
alias reboot='shutdown -r'
###################################################################### alias end ##############################################################################################


###################################################################### function start #########################################################################################
export logable="true"
# This function is used to echo info when develop this bash
function debugLog(){

	if [ $logable = "true" ] ;then
		
		echo $1;
	fi

}

# This function is used to pull debuglog for mtk debuglogger tool
function pullLog(){
	
	local destDirName=mtklog_`date +%Y%m%d_%H%M`_exception
	
	debugLog $destDirName
	
	adb shell pull /../sdcard/emulated/0/debuglogger ./$destDirName

}

# This function is used to convert argb color to hex color
function argbColorToHexColor(){

	local result=""

	for i in $@;do

		result=$result`printf "%-x" $i`
	done
	
	echo $result|clip||echo $result|xsel||echo $result|pbcopy

	printf "The result is :\n\n$result\nAnd cliped to clipboard"
}

###################################################################### function end #########################################################################################
