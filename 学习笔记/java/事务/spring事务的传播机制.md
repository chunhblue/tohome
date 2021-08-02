 spring事务的传播机制及原因分析; 

PROPAGATION_REQUIRED -- 支持当前事务，如果当前没有事务，就新建一个事务。这是最常见的选择。
PROPAGATION_SUPPORTS -- 支持当前事务，如果当前没有事务，就以非事务方式执行。
PROPAGATION_MANDATORY -- 支持当前事务，如果当前没有事务，就抛出异常。
PROPAGATION_REQUIRES_NEW -- 新建事务，如果当前存在事务，把当前事务挂起。
PROPAGATION_NOT_SUPPORTED -- 以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。
PROPAGATION_NEVER -- 以非事务方式执行，如果当前存在事务，则抛出异常。
PROPAGATION_NESTED -- 如果当前存在事务，则在嵌套事务内执行。如果当前没有事务，则进行与PROPAGATION_REQUIRED类似的操作。 

 spring默认的是PROPAGATION_REQUIRED机制, 

如果方法A标注了注解@Transactional 是完全没问题的,执行的时候传播给方法B,因为方法A开启了事务,线程内的connection的属性autoCommit=false,并且执行到方法B时,事务传播依然是生效的,得到的还是方法A的connection,autoCommit还是为false,所以事务生效;反之,如果方法A没有注解@Transactional 时,是不受事务管理的,autoCommit=true,那么传播给方法B的也为true,执行完自动提交,即使B标注了@Transactional ;
 **在一个Service内部，事务方法之间的嵌套调用，普通方法和事务方法之间的嵌套调用，都不会开启新的事务.是因为spring采用动态代理机制来实现事务控制，而动态代理最终都是要调用原始对象的，而原始对象在去调用方法时，是不会再触发代理了！** 