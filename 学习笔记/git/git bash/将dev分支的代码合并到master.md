## 一、在dev分支上运行以下命令

```
　　1. git add . // 暂存所有更改
　　2. git commit -m "更改的备注信息" // 将修改 提交到本地仓库，双引号内是提交的备注信息
　　3. git pull origin dev // 拉取远程dev分支代码
　　4. git push origin dev // 将本地修改的代码提交到远程的dev分支上
　　5. git checkout master // 切换到master分支
```

## 二、在master分支上运行以下命令

```
　　1. git merge dev // 将dev分支的代码合并到master上
　  2. git push origin master // 将当前的更改推送到远程的master分支上
执行完以上命令，此时dev分支与master分支的代码已同步。
```

