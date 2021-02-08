# item-master

item-master

## 按钮颜色说明

* 添加、增加、选择、修改、检索：蓝色

* 查看、评审、审核：橘黄色

* 成功：绿色

* 失败、删除、取消、退出、驳回：红色

* excel导出：浅蓝色

## 权限

* 基于角色管理，每个用户可以拥有多个角色

* 每个角色可以指定不同的菜单、权限、资源

    * 菜单与权限挂钩

    * 资源分为多个组，每个组由"事业部、部、DPT、店铺"组成

* 登录后session保存用户所有的角色
* 基本权限、特殊权限、代审的关系

    * 基本权限：通过角色授权实现的，用户可拥有的权限
    * 特殊权限：某些情况下需要扩大或缩小用户权限，授权后**基本权限失效**
    * 代审
        * 拥有代审权限的用户把自己拥有的某个角色临时授权给他人
        * 特殊权限人（系统部）可代替用户指定代审
        * 必须指定有效期，被指定人仅在有效期间登录至退出前，会拥有被授权权限
        * 代审权限在基本权限或特殊权限基础上追加

## 登录

* 验证用户名密码的正确性

* 取用户的特殊角色

* 不存在特殊角色时，使用 “职务+职种+DPT+店铺” 取得默认角色

* 取得用户的代审核角色，并验证代审角色

## 资源

* 由 事业部、部、dpt、店铺组成一个资源组

  * 其中店铺分为：自店铺（用五个*表示），营业店（99999），其他店铺（正常表示） 

* 事业部、部、dpt 属于三级联动，且默认初始化为全部

    * 事业部、部、dpt在选择时，默认为全事业部、全部、全dpt，以placeholder方式呈现

    * 全事业部保存值为999，全部与全dpt则不保存

* 当前资源未设定内容，则不向角色与资源表中增加内容。

* 选择资源时，事业部、部及dpt三个资源全选择后，在后台保存时全部保存

* 业务数据处理中将没有资源的检索，视为空

## 数据资源匹配

* 根据当前方法的权限，得到所有该权限的角色id，

* 结合session中角色id集合与上一步权限找到的角色集合 取交集

* 得到该当前角色所有资源，去重后匹配到业务查询sql中进行查询

* 数据获取 资源组之间是or的关系，资源组内是and关系

* 统一资源处理，将各个组员组内的数据变成针对业务表中dpt和store

## 资源类型

- 事业部

  	* 全事业部=999
  	* 其他事业部=00x

- 部门

  	* 全部门=NULL
 	* 其他部门=XX

- DPT

  	* 全DPT=NULL
  	* 其他dpt=XXX

- 店铺

  	* 自店铺=`*****`
  	* 营业店=99999
  	* 其他店铺=00XXX

## 资源结构的组合类型

- 事业部 + 部 + DPT + 店铺
- 事业部 + 部 + 店铺
- 事业部 + 店铺

## 资源的使用

**使用共同接口，传入操作人/指定人的角色集合和权限，得到该用户的整合后的资源组集合，其结构为 dpt+店铺号**

### 资源处理方式（DPT、店铺）

-	如果资源组为null，则返回null

-	DPT字段:
		
		* 如果：事业部=001，部=11，dpt=111 ，则DPT为111 
		* 如果：事业部=001，部=11，dpt=null，则DPT为119
		* 如果：事业部=001，部=null，dpt=null，则DPT为199
		* 如果：事业部=999，部=null，dpt=null，则DPT为999

-	店铺字段	：
		
		* 如果为`*****`,则视为自店铺，需要取出当前操作人/指定人自身所属的店铺（session中保存的user中存在store字段）
		* 如果为99999 ，视为营业店，该字段为null
		* 其他店铺值，返回该值即可 

## 各业务使用介绍

*	如果资源组为null，则不需要再去请求sql，直接返回null，因为各个业务的数据均与资源相关

*	不为null，将“整合后的资源组集合”作为查询条件传入sql中。

*	整合后的资源组中每组之间是 or 关系

*	整合后的资源组与其他检索条件是 and 关系

## 编码

### 1. DAO层代码生成

* ` mvn mybatis-generator:generate `

* 生成的代码在 `target/generated-sources/mybatis-generator `

* 需要获取主键时 `<generatedKey column="id" sqlStatement="select t_user_seq.nextval from dual" identity="true"/> `

* DATETIME类型配置 `<columnOverride column="UPDATETIME" javaType="java.util.Date" jdbcType="TIMESTAMP"/> `

* 注意事项：

	* 自增id处理 ：`<generatedKey column="id" sqlStatement="select xxx.currval from dual"  identity="true"/>`

	* 生成mapper后 需要调整insert语句 中需要增加id列，并value中增加:xxx.NEXTVAL 
	
	* 分页内容的设定，参照当前mapper中 menuMapper.xml进行调整
                         
## 链接的处理

* filter
    * `m.syspath` 定义servlet容器的contextPath，可为空
    * `m.staticpath`在上述标记基础上定义静态资源`${m.syspath}/static`
    
* application.yaml
    * `server.servlet.context-path`, 使用`m.syspath`，可为空
    
* pagecontent.properties
    * `common.syspath`，使用 filter的 `${m.syspath}/a`
    * `common.staticpath` 使用filter的 `m.staticpath`
    * `common.loginpath` 使用filter的 `${m.syspath}/a/login`
    
* commons-1.0.js
    * `common.config.baseurl` 使用 filter的 `${m.syspath}`
    * `common.config.surl` 使用 filter的 `${m.syspath}/a`
    
## 时间段验证

0. 全局开关在filter中设定，mine环境默认关闭
1. 全局验证
    * 指定时间段内全系统不可用，默认1800 ~ 2000
    * 标记了@Secure的类默认包含此验证
    * 禁用时跳转到登录页
    
2. 特定模块时间验证
    * 添加 `@TimeCheck( from = "1700", to = "2000" )`
        * 注意使用常量定义时间段
        * 时间段跨天时指定 `isNextDay = true`
        * 必要时指定返回类型 `ResultTypeEnum`
    * 禁用时跳转到index页

3. 代码验证
    * 调用 `TimeUtil.isInTime`方法验证当前时间是否在指定时间段内
    * 具体参考javadoc