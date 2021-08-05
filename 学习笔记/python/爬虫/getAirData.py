import urllib.request

import bs4
import psycopg2
import datetime
import re

def pageDown(URL):
    header = {"user-agent":"Google Chrome"}
    request = urllib.request.Request(URL,headers=header)
    response = urllib.request.urlopen(request)
    data = response.read().decode(encoding="utf-8")
    with open("Air Quality Index.html",mode="w",encoding="utf-8") as file:
        file.write(data)

#解析空气质量信息
def parsePage():
    with open("Air Quality Index.html",mode="r",encoding="utf-8") as file:
        data = file.read()
        soup = bs4.BeautifulSoup(data,"lxml")

        year_t = datetime.date.today().year
        year = str(year_t)
        # print(year)
        table = soup.find_all('table',class_='aqi-forecast__weekly-forecast-table')
        tr = table[0].find_all('tr _ngcontent-sc204="" class=""')
        # print(tr)


        #过滤节点列表中信息，封装到数组中
        ALL_DATA = []
        for item in tr:
            mouth_day_td = item.find('td')
            mouth_day_its = str(mouth_day_td)  # 将列表元素转化成字符串
            mouth_day_it = re.findall(",(.*?)<", mouth_day_its)
            mouth_t = re.findall('[A-Za-z]',str(mouth_day_it))
            day_t = re.findall('[0-9]',str(mouth_day_it))
            strMouth = ''.join(mouth_t).strip()  # 'Aug'
            day = ''.join(day_t).strip()
            mouth = convertMouth(strMouth)
            if mouth+day == '':
                mouthday = datetime.date.today()
                # 将 YYYY-MM-DD 转换成 YYYYMMDD
                mouth_day = datetime.datetime.strptime(str(mouthday),'%Y-%m-%d').strftime("%Y%m%d")
                mouth = mouth_day[4:8] # 字符串形式
            date = year+mouth+day
            # print(date)
            pollution_level_t = str(item.find('strong'))
            pollution_level_p = re.findall(">(.*?)<", pollution_level_t)
            pollution_level = ''.join(pollution_level_p)
            # print(pollution_level)

            row = {
                "structure_cd":'S00001',
                "acc_date": date,
                "pollution_level": pollution_level
            }
            ALL_DATA.append(row)
        return(ALL_DATA)

#存储数据库
def savePgsql(tr):
    con = psycopg2.connect(database="storedb_store_uat", user="postgres",
                        password="12345678", host="nribits.vicp.io", port="46399")
    cursor = con.cursor()
    try:
        # 插入之前删除表的数据
        cursor.execute("delete from public.ma8260")
        con.commit()
        print("数据表已清空")
        for item in tr:
            cursor.execute( "insert into public.ma8260 values ("
                            "'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s') "
                            % (item["acc_date"], item["pollution_level"],'','','','','','','','','','','',
                               item["structure_cd"]) )
            con.commit()
            print("插入成功!")
    except:
        raise
    finally:
        con.close()

def convertMouth(mouth):
    if mouth == 'Jan':
        return '01'
    elif mouth == 'Feb':
        return '02'
    elif mouth == 'Mar':
        return '03'
    elif mouth == 'Apr':
        return '04'
    elif mouth == 'May':
        return '05'
    elif mouth == 'Jun':
        return '06'
    elif mouth == 'Jul':
        return '07'
    elif mouth == 'Aug':
        return '08'
    elif mouth == 'Sep':
        return '09'
    elif mouth == 'Oct':
        return '10'
    elif mouth == 'Nov':
        return '11'
    elif mouth == 'Dec':
        return '12'
    else:
        return ''


pageDown("https://www.iqair.com/vietnam/ho-chi-minh-city")
ALL_DATA = parsePage()
savePgsql(ALL_DATA)