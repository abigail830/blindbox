
#!/usr/bin/env bash

echo "Stopping Service"
ps -ef |grep "blindbox.jar"|grep -v "grep" |awk '{print $2}'|xargs sudo kill -9
