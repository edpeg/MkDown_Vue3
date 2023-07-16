#!/bin/bash
# 拿到脚本所在目录

# 启动mdnote 
# 找到运行指定jar包的进程有两种方式：1：通过进程名匹配判断  2：通过进程监听的端口号来判断 ss -tnlp|grep 2
mdnote_pid=`ps -ef|grep 'java -jar .*mdnote.*.jar' | grep -v grep | awk '{print $2}'`
mdnote_filename=`find . -name mdnote*.jar`
echo "mdnote_pid:" $mdnote_pid
echo "mdnote_filename:" $mdnote_filename
# TODO 两个中括号啥意思？
if [[ "$mdnote_pid" != "" ]]
then
    kill -9 $mdnote_pid
fi
java -jar $mdnote_filename &