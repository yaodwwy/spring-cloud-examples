#!/usr/bin/env bash

#echo "正在拉取最新代码..."
#git fetch origin
#echo "拉取最新 master 分支..."
#git checkout -B master-bak origin/master
#echo "创建无历史分支..."
#git checkout --orphan latest_branch
#git config user.name Adam.Yao
#git config user.email yaodwwy@gmail.com
git add -A
git commit -m "Initial commit"
echo "本地初始化为 master..."
git branch -D master
git branch -m master
echo "强制更新远程 master..."
git push -f origin master
