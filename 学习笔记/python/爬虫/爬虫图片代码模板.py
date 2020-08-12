#!/usr/bin/env python # -*- coding:utf-8 -*-

#1、下载网页 > 网页字符串
#2、解析网页  >有价值的数据
#3、存储数据  >为分析做准备
import time
import urllib.request
import re

def pageDown(URL):
    response = urllib.request.urlopen(URL)
    data = response.read().decode("utf-8")
    return data

def parsePage(data):
    str = 'src="(.*\.jpg)"'
    regex = re.compile(str)  #生成正则表达式对象
    pathList = re.findall(regex,data) #检索图片地址
    print(len(pathList))
    n = 0
    for path in pathList:
        urllib.request.urlretrieve(path,"D:\heads ref\img\%s.jpg" % (n))    #下载图片并存储
        n+=1
        print('第%d张图片下载完成' % (n))
        time.sleep(1)  # 自定义延时

page = pageDown("http://www.48679.com/html/9251.html")
parsePage(page)