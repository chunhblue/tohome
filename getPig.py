import requests
import re
import time


# 请求函数
def request_get(url, ret_type="text", timeout=5, encoding="GBK"):
    headers = {
        "User-Agent": "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36"
    }
    res = requests.get(url=url, headers=headers, timeout=timeout)
    res.encoding = encoding
    if ret_type == "text":
        return res.text
    elif ret_type == "image":
        return res.content


# 抓取函数
def main():
    url = "http://www.netbian.com/mei/index.htm"
    text = request_get(url)
    format(text)


# 解析函数
def format(text):
    origin_text = split_str(text, '<div class="list">', '<div class="page">')
    pattern = re.compile('href="(.*?)"')
    hrefs = pattern.findall(origin_text)
    hrefs = [i for i in hrefs if i.find("desk") > 0]
    for href in hrefs:
        url = "http://www.netbian.com{href}"
        print("正在下载：{url}")
        text = request_get(url)
        format_detail(text)


def split_str(text, s_html, e_html):
    start = text.find(s_html) + len(e_html)
    end = text.find(e_html)
    origin_text = text[start:end]

    return origin_text


def format_detail(text):
    origin_text = split_str(text, '<div class="pic">', '<div class="pic-down">')
    pattern = re.compile('src="(.*?)"')
    image_src = pattern.search(origin_text).group(1)
    # 保存图片
    save_image(image_src)


# 存储函数
def save_image(image_src):
    content = request_get(image_src, "image")
    with open("{str(time.time())}.jpg", "wb") as f:
        f.write(content)
        print("图片保存成功")


if __name__ == '__main__':
    main()