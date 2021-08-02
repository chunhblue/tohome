 方法1:
直接修改 

```
git remote set-url origin NewGitURL
```

 方法2:
删掉旧的再添加新的 

```
git remote rm origin
git remote add origin NewGitURL
```

 方法3:
修改config文件
如果项目有加入版本控制，那可以到项目的根目录下，查看隐藏文件夹， 发现.git文件夹，找到其中的config文件，就可以修改其中的git remote origin地址了。 

查看git远程仓库地址:

- 进入项目的根目录，执行git remote -v

