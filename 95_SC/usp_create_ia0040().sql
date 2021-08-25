-- PROCEDURE: public.usp_create_ia0040()

-- DROP PROCEDURE public.usp_create_ia0040();

CREATE OR REPLACE PROCEDURE public.usp_create_ia0040(
	)
LANGUAGE 'plpgsql'
AS $BODY$
declare
    -----------------------------------------------------------------------------------------
    -- 变量定义
    -----------------------------------------------------------------------------------------
    step_no int;                                --步骤编号
    rtn_rows int;                               --SQL语句执行行数
    rtn_code text;                              --错误代码
    msg text;                                   --错误信息
    para_value varchar(100);                    --JOB参数显示用字符串
    ls_nr_date char(8);                   		--当前业务日
	-- add by ty 20210513 start 
	ls_tran_date timestamp(6);					--销售日期条件
	ls_next_tran_date timestamp(6);				--销售日期条件(下一天)
	-- add by ty 20210513 end
	

begin
    ----------------------------------------------------------------------------------------
    -- 变量初始化
    ----------------------------------------------------------------------------------------
    step_no := 0;
    rtn_code := 0;

    --------------------------------------
    -- JOB开始
    --------------------------------------
    perform usp_proc_begin_log('usp_create_ia0040',para_value);

    --------------------------------------
    -- STEP1:获取当前业务日
    --------------------------------------
    select usp_step_begin_log(step_no) into step_no;

   select sp_value from cm9060 where sp_id = '0006' into ls_nr_date;
	
	-- add by ty 20210513 start
	ls_tran_date := ls_nr_date::date||' 06:00:00';
	ls_next_tran_date := ls_nr_date::date+1||' 05:59:59';
	-- add by ty 20210513 end

    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
    
    --------------------------------------
    -- STEP2:清除数据
    --------------------------------------
    select usp_step_begin_log(step_no) into step_no;
		
		-- delete by lch 20210705 start
   -- delete from ia0040 where acc_date = ls_nr_date;
		-- delete by lch 20210705 end
		truncate sa0010_tmp2;
		truncate sa0020_tmp2;
		
    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
		
		--------------------------------------
    -- STEP3:将日结日期的sa0010数据放入临时表sa0010_tmp2
    --------------------------------------
		select usp_step_begin_log(step_no) into step_no;
		
		insert into sa0010_tmp2 SELECT * from sa0010 where upload_date = ls_nr_date and tran_type in ('sale', 'return');
		
    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
		
		--------------------------------------
    -- STEP4:将日结日期的sa0020数据放入临时表sa0020_tmp2
    --------------------------------------
		select usp_step_begin_log(step_no) into step_no;
		
		insert into sa0020_tmp2 SELECT a.* from sa0020 a,sa0010_tmp2 b 
						where a.acc_date = b.acc_date 
						and a.store_cd = b.store_cd 
						and a.pos_id = b.pos_id 
						and a.tran_serial_no = b.tran_serial_no;
						
    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;

	------------------------------------------------
    -- STEP5:根据 sa0010,sa0020 不包含service 表数据 插入 ia0040
    ------------------------------------------------
    select usp_step_begin_log(step_no) into step_no;

    insert into ia0040(
				acc_date,
				store_cd,
				sales_amt, 
				in_service, 
                "6h_8h", 
                "8h_10h", 
                "10h_12h", 
                "12h_14h", 
                "14h_16h", 
                "16h_18h", 
                "18h_20h", 
                "20h_22h", 
                "22h_24h", 
                "0h_2h", 
                "2h_4h", 
                "4h_6h")
	select 		
				 a.acc_date,
				a.store_cd,
				sum(a.sale_amount-a.over_amount), 
				'20', 
                0, 
                0, 
                0, 
                0, 
                0, 
                0, 
                0, 
                0, 
                0, 
                0, 
                0, 
                0
			from  
				(select	-- modify by ty 20210513 start
					-- acc_date, 
					case when to_char(a.tran_date,'HH24miss')<'060000'
	                      then to_char(a.tran_date+ '-1 day','yyyyMMdd')
					 else to_char(a.tran_date,'yyyyMMdd')
					end as acc_date,a.sale_amount,a.over_amount,a.pos_id,a.tran_serial_no,
					-- modify by ty 20210513 end 
					a.store_cd
			from	sa0010_tmp2 a, sa0020_tmp2 b 
			where		a.acc_date = b.acc_date 
						and a.store_cd = b.store_cd 
						and a.pos_id = b.pos_id 
						and a.tran_serial_no = b.tran_serial_no
						-- modify by lch 20210630 start
				-- and a.tran_date between ls_tran_date and ls_next_tran_date
				-- modify by lch 20210630 end 
						and b.article_id not in (SELECT article_id from to_hq_tmp.service_sales ss)
			group by 
					case when to_char(a.tran_date,'HH24miss')<'060000'
	                      then to_char(a.tran_date+ '-1 day','yyyyMMdd')
					else to_char(a.tran_date,'yyyyMMdd')
					end, 
					a.store_cd,a.sale_amount,a.over_amount,a.pos_id,a.tran_serial_no) a					

	group by 	a.acc_date,
				a.store_cd
				ON CONFLICT("acc_date", "store_cd", "in_service") DO UPDATE SET sales_amt = ia0040.sales_amt+ EXCLUDED.sales_amt;
	
	get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
	
	-- add by lch 20210528 start 
	------------------------------------------------
    -- STEP6:根据 sa0010,sa0020 包含service 表数据 插入 ia0040
    ------------------------------------------------
    select usp_step_begin_log(step_no) into step_no;

    insert into ia0040(
				acc_date,
				store_cd,
				sales_amt, 
				in_service, 
                "6h_8h", 
                "8h_10h", 
                "10h_12h", 
                "12h_14h", 
                "14h_16h", 
                "16h_18h", 
                "18h_20h", 
                "20h_22h", 
                "22h_24h", 
                "0h_2h", 
                "2h_4h", 
                "4h_6h")
	select 		
				-- modify by ty 20210513 start 
				-- a.acc_date,
				case when to_char(a.tran_date,'HH24miss')<'060000'
	                      then to_char(a.tran_date+ '-1 day','yyyyMMdd')
					 else to_char(a.tran_date,'yyyyMMdd')
				end,
				-- modify by ty 20210513 end
				a.store_cd,
				sum(a.sale_amount-a.over_amount), 
				'10', 
                0, 
                0, 
                0, 
                0, 
                0, 
                0, 
                0, 
                0,  
                0, 
                0, 
                0, 
                0
	from 		sa0010_tmp2 a
-- 	where		a.tran_type in ('sale','return')  
				-- modify by lch 20210630 start
				-- and a.tran_date between ls_tran_date and ls_next_tran_date
-- 				and a.upload_date = ls_nr_date 
				-- modify by lch 20210630 end 
	group by 	/* a.acc_date, delete by ty 20210513 */
				case when to_char(a.tran_date,'HH24miss')<'060000'
	                      then to_char(a.tran_date+ '-1 day','yyyyMMdd')
					 else to_char(a.tran_date,'yyyyMMdd')
				end,
				a.store_cd
				ON CONFLICT("acc_date", "store_cd", "in_service") DO UPDATE SET sales_amt = ia0040.sales_amt+ EXCLUDED.sales_amt;
	
	get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
		
		-- add by lch 20210528 end
	
	
    ------------------------------------------------
    -- STEP7: 更新 Include Services customer_count 字段
    ------------------------------------------------
	select usp_step_begin_log(step_no) into step_no;

	update	ia0040 a 
	set		customer_count = b.count
	from  	(select	-- modify by ty 20210513 start
					-- acc_date, 
					case when to_char(tran_date,'HH24miss')<'060000'
	                      then to_char(tran_date+ '-1 day','yyyyMMdd')
					 else to_char(tran_date,'yyyyMMdd')
					end as acc_date,
					-- modify by ty 20210513 end 
					store_cd,count(*)
			from	sa0010_tmp2 
-- 			where	
					-- modify by ty 20210513 start 
					-- acc_date = ls_nr_date 
					-- modify by lch 20210630 start
					-- tran_date between ls_tran_date and ls_next_tran_date
-- 				   upload_date = ls_nr_date 
					-- modify by lch 20210630 end 
					-- modify by ty 20210513 end
					
-- 					and tran_type in ('sale','return')  
			group by 
					case when to_char(tran_date,'HH24miss')<'060000'
	                      then to_char(tran_date+ '-1 day','yyyyMMdd')
					else to_char(tran_date,'yyyyMMdd')
					end,
					store_cd) b 
	where	a.acc_date = b.acc_date 
			and a.store_cd = b.store_cd
			and a.in_service = '10';
			
			get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
			
			-- add by lch 20210528 start
			 ------------------------------------------------
    -- STEP8: 更新 Exclude Services customer_count 字段
    ------------------------------------------------
	select usp_step_begin_log(step_no) into step_no;

	update	ia0040 a 
	set		customer_count = b.count
	from  	(select count(c.*),c.acc_date,c.store_cd from (select	-- modify by ty 20210513 start
					-- acc_date, 
					case when to_char(a.tran_date,'HH24miss')<'060000'
	                      then to_char(a.tran_date+ '-1 day','yyyyMMdd')
					 else to_char(a.tran_date,'yyyyMMdd')
					end as acc_date,a.pos_id,a.tran_serial_no, 
					-- modify by ty 20210513 end 
					a.store_cd
			from	sa0010_tmp2 a, sa0020_tmp2 b 
			where		a.acc_date = b.acc_date 
						-- modify by lch 20210630 start
				-- and a.tran_date between ls_tran_date and ls_next_tran_date
-- 				and a.upload_date = ls_nr_date 
				-- modify by lch 20210630 end 
						and a.store_cd = b.store_cd 
						and a.pos_id = b.pos_id 
						and a.tran_serial_no = b.tran_serial_no
-- 						and a.tran_type in ('sale', 'return')
						and b.article_id not in (SELECT article_id from to_hq_tmp.service_sales ss)
			group by 
					case when to_char(a.tran_date,'HH24miss')<'060000'
	                      then to_char(a.tran_date+ '-1 day','yyyyMMdd')
					else to_char(a.tran_date,'yyyyMMdd')
					end,a.pos_id,a.tran_serial_no, 
					a.store_cd) c GROUP BY c.acc_date,c.store_cd) b 
	where	a.acc_date = b.acc_date 
			and a.store_cd = b.store_cd
			and a.in_service = '20';

	get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
		-- add by lch 20210528 end

-- delete by lch 20210708 start
	------------------------------------------------
    -- STEP9: 更新 Include Services psd 字段
    ------------------------------------------------
	/*select usp_step_begin_log(step_no) into step_no;

	update	ia0040 a 
	set		psd = b.avg 
	from  	(select store_cd, 
					avg(sale_amount-over_amount) 
			from 	sa0010 
			where	-- modify by ty 20210513 start 
					-- acc_date = ls_nr_date 
					-- or acc_date::date >= (ls_nr_date::date - 28)
					-- delete by lch 20210628 start
-- 					((tran_date between ls_tran_date and ls_next_tran_date)
-- 					or tran_date::date >= (ls_nr_date::date - 28))
-- 					and tran_type in ('sale', 'return')
					-- delete by lch 20210628 end
					-- modify by ty 20210513 end
					
					-- add by lch 20210628 start
					tran_date between (ls_tran_date - interval '28 day') and ls_next_tran_date
					-- add by lch 20210628 end
			group by store_cd) b 
	where	a.store_cd = b.store_cd
			and a.in_service = '10';

	get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;*/
		

		------------------------------------------------
    -- STEP10: 更新 Exclude Services psd 字段
    ------------------------------------------------
	/*select usp_step_begin_log(step_no) into step_no;

	update	ia0040 a 
	set		psd = b.avg
	from  	(select c.store_cd,avg(c.sale_amount-c.over_amount) from
	(select	
					case when to_char(a.tran_date,'HH24miss')<'060000'
	                      then to_char(a.tran_date+ '-1 day','yyyyMMdd')
					 else to_char(a.tran_date,'yyyyMMdd')
					end as acc_date,a.sale_amount,a.over_amount,a.pos_id,a.tran_serial_no,
					a.store_cd
			from	sa0010 a, sa0020 b 
			where		a.acc_date = b.acc_date 
						and a.store_cd = b.store_cd 
						and a.pos_id = b.pos_id 
						and a.tran_serial_no = b.tran_serial_no
						-- delete by lch 20210628 start
-- 						and ((tran_date between ls_tran_date and ls_next_tran_date)
-- 							or tran_date::date >= (ls_nr_date::date - 28))
							-- delete by lch 20210628 end
							-- add by lch 20210628 start
					and tran_date between (ls_tran_date - interval '28 day') and ls_next_tran_date
					-- add by lch 20210628 end
						and a.tran_type in ('sale', 'return')
						and b.article_id not in (SELECT article_id from to_hq_tmp.service_sales ss)
			group by 
					case when to_char(a.tran_date,'HH24miss')<'060000'
	                      then to_char(a.tran_date+ '-1 day','yyyyMMdd')
					else to_char(a.tran_date,'yyyyMMdd')
					end, 
					a.store_cd,a.sale_amount,a.over_amount,a.pos_id,a.tran_serial_no) c 
					GROUP BY c.store_cd) b 
	where	a.store_cd = b.store_cd
			and a.in_service = '10';
			

	get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no; */
		-- delete by lch 20210708 end
	
	
	------------------------------------------------
    -- STEP11: 更新Include Services "12h_14h" 字段
    ------------------------------------------------
	select usp_step_begin_log(step_no) into step_no;

	update	ia0040 a 
	set		"12h_14h" = b.sum 
	from  	(select	
					-- modify by ty 20210513 start
					-- acc_date,
					case when to_char(tran_date,'HH24miss')<'060000'
							  then to_char(tran_date+ '-1 day','yyyyMMdd')
						 else to_char(tran_date,'yyyyMMdd')
					end as acc_date,
					-- modify by ty 20210513 end, 
					store_cd, 
					sum(sale_amount-over_amount) 
			from	sa0010_tmp2 
			where	
					-- modify by ty 20210513 start 
					-- acc_date = ls_nr_date 
					-- modify by lch 20210630 start
					-- tran_date between ls_tran_date and ls_next_tran_date
-- 				   upload_date = ls_nr_date 
					-- modify by lch 20210630 end 
-- 					and tran_type in ('sale', 'return')
					-- modify by ty 20210513 end
					 to_char(tran_date::timestamp, 'HH24MISS')>='120000' 
					and to_char(tran_date::timestamp, 'HH24MISS')<'140000' 
			group by 
					case when to_char(tran_date,'HH24miss')<'060000'
							  then to_char(tran_date+ '-1 day','yyyyMMdd')
						 else to_char(tran_date,'yyyyMMdd')
					end, 
					store_cd) b 
	where	a.acc_date = b.acc_date 
			and a.store_cd = b.store_cd
			and a.in_service = '10';

	get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
		
		
		------------------------------------------------
    -- STEP12: 更新Exclude Services "12h_14h" 字段
    ------------------------------------------------
	select usp_step_begin_log(step_no) into step_no;

	update	ia0040 a 
	set		"12h_14h" = b.sum
	from  	(select c.acc_date,c.store_cd,sum(c.sale_amount- c.over_amount) from
	(select	
					-- modify by ty 20210513 start
					-- acc_date,
					case when to_char(tran_date,'HH24miss')<'060000'
							  then to_char(tran_date+ '-1 day','yyyyMMdd')
						 else to_char(tran_date,'yyyyMMdd')
					end as acc_date,
					-- modify by ty 20210513 end, 
					a.store_cd, 
					-- modify by lch 20210628 start
-- 					sum(a.sale_amount-over_amount) 
						a.sale_amount,a.over_amount,a.pos_id,a.tran_serial_no
					-- modify by lch 20210628 end, 
			from	sa0010_tmp2 a, sa0020_tmp2 b 
			where		a.acc_date = b.acc_date
						-- modify by lch 20210630 start
				-- and a.tran_date between ls_tran_date and ls_next_tran_date
-- 				and a.upload_date = ls_nr_date 
				-- modify by lch 20210630 end 	
						and a.store_cd = b.store_cd 
						and a.pos_id = b.pos_id 
						and a.tran_serial_no = b.tran_serial_no
-- 						and a.tran_type in ('sale', 'return')
						-- modify by lch 20210628 start
-- 						and b.dep_cd != '03' 
						and b.article_id not in (SELECT article_id from to_hq_tmp.service_sales ss)
						-- modify by lch 20210628 end,
						and to_char(a.tran_date::timestamp, 'HH24MISS')>='120000' 
					and to_char(a.tran_date::timestamp, 'HH24MISS')<'140000' 
			group by 
					case when to_char(a.tran_date,'HH24miss')<'060000'
	                      then to_char(a.tran_date+ '-1 day','yyyyMMdd')
					else to_char(a.tran_date,'yyyyMMdd')
					end, 
					-- add by lch 20210628 start
					a.sale_amount,a.over_amount,a.pos_id,a.tran_serial_no,
					-- add by lch 20210628 end
					a.store_cd) c GROUP BY c.acc_date,c.store_cd ) b 
	where	a.acc_date = b.acc_date 
			and a.store_cd = b.store_cd
			and a.in_service = '20';

	get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
	
	------------------------------------------------
    -- STEP13: 更新 Include Services "14h_16h" 字段
    ------------------------------------------------
	select usp_step_begin_log(step_no) into step_no;
	
	update	ia0040 a 
	set		"14h_16h" = b.sum 
	from  	(select	-- modify by ty 20210513 start
					-- acc_date,
					case when to_char(tran_date,'HH24miss')<'060000'
							  then to_char(tran_date+ '-1 day','yyyyMMdd')
						 else to_char(tran_date,'yyyyMMdd')
					end as acc_date,
					-- modify by ty 20210513 end,
					store_cd, 
					sum(sale_amount-over_amount) 
			from	sa0010_tmp2 
			where	
					-- modify by ty 20210513 start 
					-- acc_date = ls_nr_date 
					-- modify by lch 20210630 start
					-- tran_date between ls_tran_date and ls_next_tran_date
-- 				   upload_date = ls_nr_date 
					-- modify by lch 20210630 end 
-- 					and tran_type in ('sale', 'return')
					-- modify by ty 20210513 end
					to_char(tran_date::timestamp, 'HH24MISS')>='140000' 
					and to_char(tran_date::timestamp, 'HH24MISS')<'160000' 
			group by 
					case when to_char(tran_date,'HH24miss')<'060000'
							  then to_char(tran_date+ '-1 day','yyyyMMdd')
						 else to_char(tran_date,'yyyyMMdd')
					end, 
					store_cd) b 
	where	a.acc_date = b.acc_date 
			and a.store_cd = b.store_cd
			and a.in_service = '10';
	
	get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
	
	
	------------------------------------------------
    -- STEP14: 更新Exclude Services "14h_16h" 字段
    ------------------------------------------------
	select usp_step_begin_log(step_no) into step_no;

	update	ia0040 a 
	set		"14h_16h" = b.sum
	from  	(select c.acc_date,c.store_cd,sum(c.sale_amount- c.over_amount) from
	(select	
					-- modify by ty 20210513 start
					-- acc_date,
					case when to_char(tran_date,'HH24miss')<'060000'
							  then to_char(tran_date+ '-1 day','yyyyMMdd')
						 else to_char(tran_date,'yyyyMMdd')
					end as acc_date,
					-- modify by ty 20210513 end, 
					a.store_cd, 
					-- modify by lch 20210628 start
-- 					sum(a.sale_amount-over_amount) 
						a.sale_amount,a.over_amount,a.pos_id,a.tran_serial_no
					-- modify by lch 20210628 end, 
			from	sa0010_tmp2 a, sa0020_tmp2 b 
			where		a.acc_date = b.acc_date 
						-- modify by lch 20210630 start
				-- and a.tran_date between ls_tran_date and ls_next_tran_date
-- 				and a.upload_date = ls_nr_date 
				-- modify by lch 20210630 end 
						and a.store_cd = b.store_cd 
						and a.pos_id = b.pos_id 
						and a.tran_serial_no = b.tran_serial_no
-- 						and a.tran_type in ('sale', 'return')
						-- modify by lch 20210628 start
-- 						and b.dep_cd != '03' 
						and b.article_id not in (SELECT article_id from to_hq_tmp.service_sales ss)
						-- modify by lch 20210628 end,
						and to_char(a.tran_date::timestamp, 'HH24MISS')>='140000' 
					and to_char(a.tran_date::timestamp, 'HH24MISS')<'160000' 
			group by 
					case when to_char(a.tran_date,'HH24miss')<'060000'
	                      then to_char(a.tran_date+ '-1 day','yyyyMMdd')
					else to_char(a.tran_date,'yyyyMMdd')
					end, 
					-- add by lch 20210628 start
					a.sale_amount,a.over_amount,a.pos_id,a.tran_serial_no,
					-- add by lch 20210628 end
					a.store_cd) c GROUP BY c.acc_date,c.store_cd ) b 
	where	a.acc_date = b.acc_date 
			and a.store_cd = b.store_cd
			and a.in_service = '20';

	get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
	
	
	------------------------------------------------
    -- STEP15: 更新Include Services "16h_18h" 字段
    ------------------------------------------------
	select usp_step_begin_log(step_no) into step_no;
	
	update	ia0040 a 
	set		"16h_18h" = b.sum 
	from  	(select	-- modify by ty 20210513 start
					-- acc_date,
					case when to_char(tran_date,'HH24miss')<'060000'
							  then to_char(tran_date+ '-1 day','yyyyMMdd')
						 else to_char(tran_date,'yyyyMMdd')
					end as acc_date,
					-- modify by ty 20210513 end,
					store_cd, 
					sum(sale_amount-over_amount) 
			from	sa0010_tmp2 
			where	
					-- modify by ty 20210513 start 
					-- acc_date = ls_nr_date 
					-- modify by lch 20210630 start
					-- tran_date between ls_tran_date and ls_next_tran_date
-- 				   upload_date = ls_nr_date 
					-- modify by lch 20210630 end 
-- 					and tran_type in ('sale', 'return')
					-- modify by ty 20210513 end
					to_char(tran_date::timestamp, 'HH24MISS')>='160000' 
					and to_char(tran_date::timestamp, 'HH24MISS')<'180000' 
			group by case when to_char(tran_date,'HH24miss')<'060000'
							  then to_char(tran_date+ '-1 day','yyyyMMdd')
						 else to_char(tran_date,'yyyyMMdd')
					end, 
					store_cd) b 
	where	a.acc_date = b.acc_date 
			and a.store_cd = b.store_cd
			and a.in_service = '10';
	
	get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
		
		
		------------------------------------------------
    -- STEP16: 更新Exclude Services "16h_18h" 字段
    ------------------------------------------------
	select usp_step_begin_log(step_no) into step_no;

	update	ia0040 a 
	set		"16h_18h" = b.sum
	from  	(select c.acc_date,c.store_cd,sum(c.sale_amount- c.over_amount) from
	(select	
					-- modify by ty 20210513 start
					-- acc_date,
					case when to_char(tran_date,'HH24miss')<'060000'
							  then to_char(tran_date+ '-1 day','yyyyMMdd')
						 else to_char(tran_date,'yyyyMMdd')
					end as acc_date,
					-- modify by ty 20210513 end, 
					a.store_cd, 
					-- modify by lch 20210628 start
-- 					sum(a.sale_amount-over_amount) 
						a.sale_amount,a.over_amount,a.pos_id,a.tran_serial_no
					-- modify by lch 20210628 end, 
			from	sa0010_tmp2 a, sa0020_tmp2 b 
			where		a.acc_date = b.acc_date 
						-- modify by lch 20210630 start
				-- and a.tran_date between ls_tran_date and ls_next_tran_date
-- 				and a.upload_date = ls_nr_date 
				-- modify by lch 20210630 end 
						and a.store_cd = b.store_cd 
						and a.pos_id = b.pos_id 
						and a.tran_serial_no = b.tran_serial_no
-- 						and a.tran_type in ('sale', 'return')
						-- modify by lch 20210628 start
-- 						and b.dep_cd != '03' 
						and b.article_id not in (SELECT article_id from to_hq_tmp.service_sales ss)
						-- modify by lch 20210628 end,
						and to_char(a.tran_date::timestamp, 'HH24MISS')>='160000' 
					and to_char(a.tran_date::timestamp, 'HH24MISS')<'180000' 
			group by 
					case when to_char(a.tran_date,'HH24miss')<'060000'
	                      then to_char(a.tran_date+ '-1 day','yyyyMMdd')
					else to_char(a.tran_date,'yyyyMMdd')
					end, 
						-- add by lch 20210628 start
					a.sale_amount,a.over_amount,a.pos_id,a.tran_serial_no,
					-- add by lch 20210628 end
					a.store_cd) c GROUP BY c.acc_date,c.store_cd ) b 
	where	a.acc_date = b.acc_date 
			and a.store_cd = b.store_cd
			and a.in_service = '20';

	get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
	

	------------------------------------------------
    -- STEP17: 更新Include Services "18h_20h" 字段
    ------------------------------------------------
	select usp_step_begin_log(step_no) into step_no;
	
	update	ia0040 a 
	set		"18h_20h" = b.sum
	from  	(select	-- modify by ty 20210513 start
					-- acc_date,
					case when to_char(tran_date,'HH24miss')<'060000'
							  then to_char(tran_date+ '-1 day','yyyyMMdd')
						 else to_char(tran_date,'yyyyMMdd')
					end as acc_date,
					-- modify by ty 20210513 end,
					store_cd, 
					sum(sale_amount-over_amount) 
			from	sa0010_tmp2 
			where	
					-- modify by ty 20210513 start 
					-- acc_date = ls_nr_date 
					-- modify by lch 20210630 start
					-- tran_date between ls_tran_date and ls_next_tran_date
-- 				   upload_date = ls_nr_date 
					-- modify by lch 20210630 end 
-- 					and tran_type in ('sale', 'return')
					-- modify by ty 20210513 end
					 to_char(tran_date::timestamp, 'HH24MISS')>='180000' 
					and to_char(tran_date::timestamp, 'HH24MISS')<'200000' 
			group by case when to_char(tran_date,'HH24miss')<'060000'
							  then to_char(tran_date+ '-1 day','yyyyMMdd')
						 else to_char(tran_date,'yyyyMMdd')
					end, 
					store_cd) b 
	where	a.acc_date = b.acc_date 
			and a.store_cd = b.store_cd
			and a.in_service = '10';
	
	get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
		
		
			------------------------------------------------
    -- STEP18: 更新Exclude Services "18h_20h" 字段
    ------------------------------------------------
	select usp_step_begin_log(step_no) into step_no;

	update	ia0040 a 
	set		"18h_20h" = b.sum 
	from  	(select c.acc_date,c.store_cd,sum(c.sale_amount- c.over_amount) from
	(select	
					-- modify by ty 20210513 start
					-- acc_date,
					case when to_char(tran_date,'HH24miss')<'060000'
							  then to_char(tran_date+ '-1 day','yyyyMMdd')
						 else to_char(tran_date,'yyyyMMdd')
					end as acc_date,
					-- modify by ty 20210513 end, 
					a.store_cd, 
				-- modify by lch 20210628 start
-- 					sum(a.sale_amount-over_amount) 
						a.sale_amount,a.over_amount,a.pos_id,a.tran_serial_no
					-- modify by lch 20210628 end, 
			from	sa0010_tmp2 a, sa0020_tmp2 b 
			where		a.acc_date = b.acc_date 
						-- modify by lch 20210630 start
				-- and a.tran_date between ls_tran_date and ls_next_tran_date
-- 				and a.upload_date = ls_nr_date 
				-- modify by lch 20210630 end 
						and a.store_cd = b.store_cd 
						and a.pos_id = b.pos_id 
						and a.tran_serial_no = b.tran_serial_no
-- 						and a.tran_type in ('sale', 'return')
						-- modify by lch 20210628 start
-- 						and b.dep_cd != '03' 
						and b.article_id not in (SELECT article_id from to_hq_tmp.service_sales ss)
						-- modify by lch 20210628 end,
						and to_char(a.tran_date::timestamp, 'HH24MISS')>='180000' 
					and to_char(a.tran_date::timestamp, 'HH24MISS')<'200000' 
			group by 
					case when to_char(a.tran_date,'HH24miss')<'060000'
	                      then to_char(a.tran_date+ '-1 day','yyyyMMdd')
					else to_char(a.tran_date,'yyyyMMdd')
					end, 
						-- add by lch 20210628 start
					a.sale_amount,a.over_amount,a.pos_id,a.tran_serial_no,
					-- add by lch 20210628 end
					a.store_cd) c GROUP BY c.acc_date,c.store_cd ) b 
	where	a.acc_date = b.acc_date 
			and a.store_cd = b.store_cd
			and a.in_service = '20';

	get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
	
	
	------------------------------------------------
    -- STEP19: 更新Include Services  "20h_22h" 字段
    ------------------------------------------------
	select usp_step_begin_log(step_no) into step_no;
	
	update	ia0040 a 
	set		"20h_22h" = b.sum 
	from  	(select	-- modify by ty 20210513 start
					-- acc_date,
					case when to_char(tran_date,'HH24miss')<'060000'
							  then to_char(tran_date+ '-1 day','yyyyMMdd')
						 else to_char(tran_date,'yyyyMMdd')
					end as acc_date,
					-- modify by ty 20210513 end,
					store_cd, 
					sum(sale_amount-over_amount) 
			from	sa0010_tmp2 
			where	
					-- modify by ty 20210513 start 
					-- acc_date = ls_nr_date 
					-- modify by lch 20210630 start
					-- tran_date between ls_tran_date and ls_next_tran_date
-- 				   upload_date = ls_nr_date 
					-- modify by lch 20210630 end 
-- 					and tran_type in ('sale', 'return')
					-- modify by ty 20210513 end
					to_char(tran_date::timestamp, 'HH24MISS')>='200000' 
					and to_char(tran_date::timestamp, 'HH24MISS')<'220000' 
			group by case when to_char(tran_date,'HH24miss')<'060000'
							  then to_char(tran_date+ '-1 day','yyyyMMdd')
						 else to_char(tran_date,'yyyyMMdd')
					end, 
					store_cd) b 
	where	a.acc_date = b.acc_date 
			and a.store_cd = b.store_cd
			and a.in_service = '10';
	
	get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
		
		
			------------------------------------------------
    -- STEP20: 更新Exclude Services "20h_22h" 字段
    ------------------------------------------------
	select usp_step_begin_log(step_no) into step_no;

	update	ia0040 a 
	set		"20h_22h" = b.sum  
	from  	(select c.acc_date,c.store_cd,sum(c.sale_amount- c.over_amount) from
	(select	
					-- modify by ty 20210513 start
					-- acc_date,
					case when to_char(tran_date,'HH24miss')<'060000'
							  then to_char(tran_date+ '-1 day','yyyyMMdd')
						 else to_char(tran_date,'yyyyMMdd')
					end as acc_date,
					-- modify by ty 20210513 end, 
					a.store_cd, 
				-- modify by lch 20210628 start
-- 					sum(a.sale_amount-over_amount) 
						a.sale_amount,a.over_amount,a.pos_id,a.tran_serial_no
					-- modify by lch 20210628 end, 
			from	sa0010_tmp2 a, sa0020_tmp2 b 
			where		a.acc_date = b.acc_date 
						-- modify by lch 20210630 start
				-- and a.tran_date between ls_tran_date and ls_next_tran_date
-- 				and a.upload_date = ls_nr_date 
				-- modify by lch 20210630 end 
						and a.store_cd = b.store_cd 
						and a.pos_id = b.pos_id 
						and a.tran_serial_no = b.tran_serial_no
-- 						and a.tran_type in ('sale', 'return')
						-- modify by lch 20210628 start
-- 						and b.dep_cd != '03' 
						and b.article_id not in (SELECT article_id from to_hq_tmp.service_sales ss)
						-- modify by lch 20210628 end,
						and to_char(a.tran_date::timestamp, 'HH24MISS')>='200000' 
					and to_char(a.tran_date::timestamp, 'HH24MISS')<'220000' 
			group by 
					case when to_char(a.tran_date,'HH24miss')<'060000'
	                      then to_char(a.tran_date+ '-1 day','yyyyMMdd')
					else to_char(a.tran_date,'yyyyMMdd')
					end, 
						-- add by lch 20210628 start
					a.sale_amount,a.over_amount,a.pos_id,a.tran_serial_no,
					-- add by lch 20210628 end
					a.store_cd) c GROUP BY c.acc_date,c.store_cd ) b 
	where	a.acc_date = b.acc_date 
			and a.store_cd = b.store_cd
			and a.in_service = '20';

	get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
	

	------------------------------------------------
    -- STEP21: 更新Include Services "22h_24h" 字段
    ------------------------------------------------
	select usp_step_begin_log(step_no) into step_no;
	
	update	ia0040 a 
	set		"22h_24h" = b.sum 
	from  	(select	-- modify by ty 20210513 start
					-- acc_date,
					case when to_char(tran_date,'HH24miss')<'060000'
							  then to_char(tran_date+ '-1 day','yyyyMMdd')
						 else to_char(tran_date,'yyyyMMdd')
					end as acc_date,
					-- modify by ty 20210513 end,
					store_cd, 
					sum(sale_amount-over_amount) 
			from	sa0010_tmp2 
			where	
					-- modify by ty 20210513 start 
					-- acc_date = ls_nr_date 
					-- modify by lch 20210630 start
					-- tran_date between ls_tran_date and ls_next_tran_date
-- 				   upload_date = ls_nr_date 
					-- modify by lch 20210630 end 
-- 					and tran_type in ('sale', 'return')
					-- modify by ty 20210513 end
					 to_char(tran_date::timestamp, 'HH24MISS')>='220000' 
			group by case when to_char(tran_date,'HH24miss')<'060000'
							  then to_char(tran_date+ '-1 day','yyyyMMdd')
						 else to_char(tran_date,'yyyyMMdd')
					end , 
					store_cd) b 
	where	a.acc_date = b.acc_date 
			and a.store_cd = b.store_cd
			and a.in_service = '10';
	
	get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
		
		
			------------------------------------------------
    -- STEP22: 更新Exclude Services "22h_24h" 字段
    ------------------------------------------------
	select usp_step_begin_log(step_no) into step_no;

	update	ia0040 a 
	set		"22h_24h" = b.sum
	from  	(select c.acc_date,c.store_cd,sum(c.sale_amount- c.over_amount) from
	(select	
					-- modify by ty 20210513 start
					-- acc_date,
					case when to_char(tran_date,'HH24miss')<'060000'
							  then to_char(tran_date+ '-1 day','yyyyMMdd')
						 else to_char(tran_date,'yyyyMMdd')
					end as acc_date,
					-- modify by ty 20210513 end, 
					a.store_cd, 
					-- modify by lch 20210628 start
-- 					sum(a.sale_amount-over_amount) 
						a.sale_amount,a.over_amount,a.pos_id,a.tran_serial_no
					-- modify by lch 20210628 end, 
			from	sa0010_tmp2 a, sa0020_tmp2 b 
			where		a.acc_date = b.acc_date 
						-- modify by lch 20210630 start
				-- and a.tran_date between ls_tran_date and ls_next_tran_date
-- 				and a.upload_date = ls_nr_date 
				-- modify by lch 20210630 end 
						and a.store_cd = b.store_cd 
						and a.pos_id = b.pos_id 
						and a.tran_serial_no = b.tran_serial_no
-- 						and a.tran_type in ('sale', 'return')
						-- modify by lch 20210628 start
-- 						and b.dep_cd != '03' 
						and b.article_id not in (SELECT article_id from to_hq_tmp.service_sales ss)
						-- modify by lch 20210628 end,
						and to_char(a.tran_date::timestamp, 'HH24MISS')>='220000' 
					and to_char(a.tran_date::timestamp, 'HH24MISS')<'240000' 
			group by 
					case when to_char(a.tran_date,'HH24miss')<'060000'
	                      then to_char(a.tran_date+ '-1 day','yyyyMMdd')
					else to_char(a.tran_date,'yyyyMMdd')
					end, 
						-- add by lch 20210628 start
					a.sale_amount,a.over_amount,a.pos_id,a.tran_serial_no,
					-- add by lch 20210628 end
					a.store_cd) c GROUP BY c.acc_date,c.store_cd ) b 
	where	a.acc_date = b.acc_date 
			and a.store_cd = b.store_cd
			and a.in_service = '20';

	get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;

	------------------------------------------------
    -- STEP23: 更新Include Services "0h_2h" 字段
    ------------------------------------------------
	select usp_step_begin_log(step_no) into step_no;
	
	update	ia0040 a 
	set		"0h_2h" = b.sum 
	from  	
	(select	-- modify by ty 20210513 start
					-- acc_date,
					case when to_char(tran_date,'HH24miss')<'060000'
							  then to_char(tran_date+ '-1 day','yyyyMMdd')
						 else to_char(tran_date,'yyyyMMdd')
					end as acc_date,
					-- modify by ty 20210513 end,
					store_cd, 
					sum(sale_amount-over_amount) 
			from	sa0010_tmp2 
			where	
					-- modify by ty 20210513 start 
					-- acc_date = ls_nr_date 
					-- modify by lch 20210630 start
					-- tran_date between ls_tran_date and ls_next_tran_date
-- 				   upload_date = ls_nr_date 
					-- modify by lch 20210630 end 
-- 					and tran_type in ('sale', 'return')
					-- modify by ty 20210513 end
					to_char(tran_date::timestamp, 'HH24MISS')>='000000' 
					and to_char(tran_date::timestamp, 'HH24MISS')<'020000' 
			group by case when to_char(tran_date,'HH24miss')<'060000'
							  then to_char(tran_date+ '-1 day','yyyyMMdd')
						 else to_char(tran_date,'yyyyMMdd')
					end, 
					store_cd) b 
	where	a.acc_date = b.acc_date 
			and a.store_cd = b.store_cd
				and a.in_service = '10';
	
	get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
		
		
			------------------------------------------------
    -- STEP24: 更新Exclude Services "0h_2h" 字段
    ------------------------------------------------
	select usp_step_begin_log(step_no) into step_no;

	update	ia0040 a 
	set		"0h_2h" = b.sum
	from  	(select c.acc_date,c.store_cd,sum(c.sale_amount- c.over_amount) from
	(select	
					-- modify by ty 20210513 start
					-- acc_date,
					case when to_char(tran_date,'HH24miss')<'060000'
							  then to_char(tran_date+ '-1 day','yyyyMMdd')
						 else to_char(tran_date,'yyyyMMdd')
					end as acc_date,
					-- modify by ty 20210513 end, 
					a.store_cd,
					-- modify by lch 20210628 start
-- 					sum(a.sale_amount-over_amount) 
						a.sale_amount,a.over_amount,a.pos_id,a.tran_serial_no
					-- modify by lch 20210628 end, 
			from	sa0010_tmp2 a, sa0020_tmp2 b 
			where		a.acc_date = b.acc_date 
						-- modify by lch 20210630 start
				-- and a.tran_date between ls_tran_date and ls_next_tran_date
-- 				and a.upload_date = ls_nr_date 
				-- modify by lch 20210630 end 	
						and a.store_cd = b.store_cd 
						and a.pos_id = b.pos_id 
						and a.tran_serial_no = b.tran_serial_no
-- 						and a.tran_type in ('sale', 'return')
						-- modify by lch 20210628 start
-- 						and b.dep_cd != '03' 
						and b.article_id not in (SELECT article_id from to_hq_tmp.service_sales ss)
						-- modify by lch 20210628 end,
						and to_char(a.tran_date::timestamp, 'HH24MISS')>='000000' 
					and to_char(a.tran_date::timestamp, 'HH24MISS')<'020000' 
			group by 
					case when to_char(a.tran_date,'HH24miss')<'060000'
	                      then to_char(a.tran_date+ '-1 day','yyyyMMdd')
					else to_char(a.tran_date,'yyyyMMdd')
					end, 
						-- add by lch 20210628 start
					a.sale_amount,a.over_amount,a.pos_id,a.tran_serial_no,
					-- add by lch 20210628 end
					a.store_cd) c GROUP BY c.acc_date,c.store_cd ) b 
	where	a.acc_date = b.acc_date 
			and a.store_cd = b.store_cd
			and a.in_service = '20';

	get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;

	------------------------------------------------
    -- STEP25: 更新Include Services "2h_4h" 字段
    ------------------------------------------------
	select usp_step_begin_log(step_no) into step_no;
	
	update	ia0040 a 
	set		"2h_4h" = b.sum 
	from  	(select	-- modify by ty 20210513 start
					-- acc_date,
					case when to_char(tran_date,'HH24miss')<'060000'
							  then to_char(tran_date+ '-1 day','yyyyMMdd')
						 else to_char(tran_date,'yyyyMMdd')
					end as acc_date,
					-- modify by ty 20210513 end, 
					store_cd, 
					sum(sale_amount-over_amount) 
			from	sa0010_tmp2 
			where	-- modify by ty 20210513 start 
					-- acc_date = ls_nr_date 
					-- modify by lch 20210630 start
					-- tran_date between ls_tran_date and ls_next_tran_date
-- 				   upload_date = ls_nr_date 
					-- modify by lch 20210630 end 
-- 					and tran_type in ('sale', 'return')
					-- modify by ty 20210513 end
					to_char(tran_date::timestamp, 'HH24MISS')>='020000' 
					and to_char(tran_date::timestamp, 'HH24MISS')<'040000' 
			group by case when to_char(tran_date,'HH24miss')<'060000'
							  then to_char(tran_date+ '-1 day','yyyyMMdd')
						 else to_char(tran_date,'yyyyMMdd')
					end, 
					store_cd) b 
	where	a.acc_date = b.acc_date 
			and a.store_cd = b.store_cd
			and a.in_service = '10';
	
	get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
		
		
		------------------------------------------------
    -- STEP26: 更新Exclude Services "2h_4h" 字段
    ------------------------------------------------
	select usp_step_begin_log(step_no) into step_no;

	update	ia0040 a 
	set		"2h_4h" = b.sum
	from  	(select c.acc_date,c.store_cd,sum(c.sale_amount- c.over_amount) from
	(select	
					-- modify by ty 20210513 start
					-- acc_date,
					case when to_char(tran_date,'HH24miss')<'060000'
							  then to_char(tran_date+ '-1 day','yyyyMMdd')
						 else to_char(tran_date,'yyyyMMdd')
					end as acc_date,
					-- modify by ty 20210513 end, 
					a.store_cd,
					-- modify by lch 20210628 start
-- 					sum(a.sale_amount-over_amount) 
						a.sale_amount,a.over_amount,a.pos_id,a.tran_serial_no
					-- modify by lch 20210628 end, 
			from	sa0010_tmp2 a, sa0020_tmp2 b 
			where		a.acc_date = b.acc_date 
						-- modify by lch 20210630 start
				-- and a.tran_date between ls_tran_date and ls_next_tran_date
-- 				and a.upload_date = ls_nr_date 
				-- modify by lch 20210630 end 
						and a.store_cd = b.store_cd 
						and a.pos_id = b.pos_id 
						and a.tran_serial_no = b.tran_serial_no
-- 						and a.tran_type in ('sale', 'return')
							-- modify by lch 20210628 start
-- 						and b.dep_cd != '03' 
						and b.article_id not in (SELECT article_id from to_hq_tmp.service_sales ss)
						-- modify by lch 20210628 end,
						and to_char(a.tran_date::timestamp, 'HH24MISS')>='020000' 
					and to_char(a.tran_date::timestamp, 'HH24MISS')<'040000' 
			group by 
					case when to_char(a.tran_date,'HH24miss')<'060000'
	                      then to_char(a.tran_date+ '-1 day','yyyyMMdd')
					else to_char(a.tran_date,'yyyyMMdd')
					end, 
						-- add by lch 20210628 start
					a.sale_amount,a.over_amount,a.pos_id,a.tran_serial_no,
					-- add by lch 20210628 end
					a.store_cd) c GROUP BY c.acc_date,c.store_cd ) b 
	where	a.acc_date = b.acc_date 
			and a.store_cd = b.store_cd
			and a.in_service = '20';

	get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
	
	------------------------------------------------
    -- STEP27: 更新Include Services "4h_6h" 字段
    ------------------------------------------------
	select usp_step_begin_log(step_no) into step_no;
	
	update	ia0040 a 
	set		"4h_6h" = b.sum 
	from  	(select	-- modify by ty 20210513 start
					-- acc_date,
					case when to_char(tran_date,'HH24miss')<'060000'
							  then to_char(tran_date+ '-1 day','yyyyMMdd')
						 else to_char(tran_date,'yyyyMMdd')
					end as acc_date,
					-- modify by ty 20210513 end, 
					store_cd, 
					sum(sale_amount-over_amount) 
			from	sa0010_tmp2 
			where	-- modify by ty 20210513 start 
					-- acc_date = ls_nr_date 
					-- modify by lch 20210630 start
					-- tran_date between ls_tran_date and ls_next_tran_date
-- 				   upload_date = ls_nr_date 
					-- modify by lch 20210630 end 
-- 					and tran_type in ('sale', 'return')
					-- modify by ty 20210513 end
					to_char(tran_date::timestamp, 'HH24MISS')>='040000' 
					and to_char(tran_date::timestamp, 'HH24MISS')<'060000' 
			group by case when to_char(tran_date,'HH24miss')<'060000'
							  then to_char(tran_date+ '-1 day','yyyyMMdd')
						 else to_char(tran_date,'yyyyMMdd')
					end, 
					store_cd) b 
	where	a.acc_date = b.acc_date 
			and a.store_cd = b.store_cd
			and a.in_service = '10';
	
	get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
		
		------------------------------------------------
    -- STEP28: 更新Exclude Services "4h_6h" 字段
    ------------------------------------------------
	select usp_step_begin_log(step_no) into step_no;

	update	ia0040 a 
	set		"4h_6h" = b.sum
	from  	(select c.acc_date,c.store_cd,sum(c.sale_amount- c.over_amount) from
	(select	
					-- modify by ty 20210513 start
					-- acc_date,
					case when to_char(tran_date,'HH24miss')<'060000'
							  then to_char(tran_date+ '-1 day','yyyyMMdd')
						 else to_char(tran_date,'yyyyMMdd')
					end as acc_date,
					-- modify by ty 20210513 end, 
					a.store_cd,
					-- modify by lch 20210628 start
-- 					sum(a.sale_amount-over_amount) 
						a.sale_amount,a.over_amount,a.pos_id,a.tran_serial_no
					-- modify by lch 20210628 end, 
			from	sa0010_tmp2 a, sa0020_tmp2 b 
			where		a.acc_date = b.acc_date 
						-- modify by lch 20210630 start
				-- and a.tran_date between ls_tran_date and ls_next_tran_date
-- 				and a.upload_date = ls_nr_date 
				-- modify by lch 20210630 end 
						and a.store_cd = b.store_cd 
						and a.pos_id = b.pos_id 
						and a.tran_serial_no = b.tran_serial_no
-- 						and a.tran_type in ('sale', 'return')
							-- modify by lch 20210628 start
-- 						and b.dep_cd != '03' 
						and b.article_id not in (SELECT article_id from to_hq_tmp.service_sales ss)
						-- modify by lch 20210628 end,
						and to_char(a.tran_date::timestamp, 'HH24MISS')>='040000' 
					and to_char(a.tran_date::timestamp, 'HH24MISS')<'060000' 
			group by 
					case when to_char(a.tran_date,'HH24miss')<'060000'
	                      then to_char(a.tran_date+ '-1 day','yyyyMMdd')
					else to_char(a.tran_date,'yyyyMMdd')
					end, 
					-- add by lch 20210628 start
					a.sale_amount,a.over_amount,a.pos_id,a.tran_serial_no,
					-- add by lch 20210628 end
					a.store_cd) c GROUP BY c.acc_date,c.store_cd ) b 
	where	a.acc_date = b.acc_date 
			and a.store_cd = b.store_cd
			and a.in_service = '20';

	get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
	
	------------------------------------------------
    -- STEP29: 更新Include Services "6h_8h" 字段
    ------------------------------------------------
	select usp_step_begin_log(step_no) into step_no;
	
	update	ia0040 a 
	set		"6h_8h" = b.sum 
	from  	(select	-- modify by ty 20210513 start
					-- acc_date,
					case when to_char(tran_date,'HH24miss')<'060000'
							  then to_char(tran_date+ '-1 day','yyyyMMdd')
						 else to_char(tran_date,'yyyyMMdd')
					end as acc_date,
					-- modify by ty 20210513 end,
					store_cd, 
					sum(sale_amount-over_amount) 
			from	sa0010_tmp2 
			where	-- modify by ty 20210513 start 
					-- acc_date = ls_nr_date 
					-- modify by lch 20210630 start
					-- tran_date between ls_tran_date and ls_next_tran_date
-- 				   upload_date = ls_nr_date 
					-- modify by lch 20210630 end 
-- 					and tran_type in ('sale', 'return')
					-- modify by ty 20210513 end
					to_char(tran_date::timestamp, 'HH24MISS')>='060000' 
					and to_char(tran_date::timestamp, 'HH24MISS')<'080000' 
			group by case when to_char(tran_date,'HH24miss')<'060000'
							  then to_char(tran_date+ '-1 day','yyyyMMdd')
						 else to_char(tran_date,'yyyyMMdd')
					end , 
					store_cd) b 
	where	a.acc_date = b.acc_date 
			and a.store_cd = b.store_cd
			and a.in_service = '10';
	
	get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
		
		------------------------------------------------
    -- STEP30: 更新Exclude Services "6h_8h" 字段
    ------------------------------------------------
	select usp_step_begin_log(step_no) into step_no;

	update	ia0040 a 
	set		"6h_8h" = b.sum
	from  	(select c.acc_date,c.store_cd,sum(c.sale_amount- c.over_amount) from
	(select	
					-- modify by ty 20210513 start
					-- acc_date,
					case when to_char(tran_date,'HH24miss')<'060000'
							  then to_char(tran_date+ '-1 day','yyyyMMdd')
						 else to_char(tran_date,'yyyyMMdd')
					end as acc_date,
					-- modify by ty 20210513 end, 
					a.store_cd, 
				-- modify by lch 20210628 start
-- 					sum(a.sale_amount-over_amount) 
						a.sale_amount,a.over_amount,a.pos_id,a.tran_serial_no
					-- modify by lch 20210628 end, 
			from	sa0010_tmp2 a, sa0020_tmp2 b 
			where		a.acc_date = b.acc_date 
						-- modify by lch 20210630 start
				-- and a.tran_date between ls_tran_date and ls_next_tran_date
-- 				and a.upload_date = ls_nr_date 
				-- modify by lch 20210630 end 
						and a.store_cd = b.store_cd 
						and a.pos_id = b.pos_id 
						and a.tran_serial_no = b.tran_serial_no
-- 						and a.tran_type in ('sale', 'return')
							-- modify by lch 20210628 start
-- 						and b.dep_cd != '03' 
						and b.article_id not in (SELECT article_id from to_hq_tmp.service_sales ss)
						-- modify by lch 20210628 end,
						and to_char(a.tran_date::timestamp, 'HH24MISS')>='060000' 
					and to_char(a.tran_date::timestamp, 'HH24MISS')<'080000' 
			group by 
					case when to_char(a.tran_date,'HH24miss')<'060000'
	                      then to_char(a.tran_date+ '-1 day','yyyyMMdd')
					else to_char(a.tran_date,'yyyyMMdd')
					end, 
					-- add by lch 20210628 start
					a.sale_amount,a.over_amount,a.pos_id,a.tran_serial_no,
					-- add by lch 20210628 end
					a.store_cd) c GROUP BY c.acc_date,c.store_cd ) b 
	where	a.acc_date = b.acc_date 
			and a.store_cd = b.store_cd
			and a.in_service = '20';

	get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
	
	------------------------------------------------
    -- STEP31: 更新Include Services "8h_10h" 字段
    ------------------------------------------------
	select usp_step_begin_log(step_no) into step_no;
	
	update	ia0040 a 
	set		"8h_10h" = b.sum 
	from  	(select	-- modify by ty 20210513 start
					-- acc_date,
					case when to_char(tran_date,'HH24miss')<'060000'
							  then to_char(tran_date+ '-1 day','yyyyMMdd')
						 else to_char(tran_date,'yyyyMMdd')
					end as acc_date,
					-- modify by ty 20210513 end,
					store_cd, 
					sum(sale_amount-over_amount) 
			from	sa0010_tmp2 
			where	-- modify by ty 20210513 start 
					-- acc_date = ls_nr_date 
					-- modify by lch 20210630 start
					-- tran_date between ls_tran_date and ls_next_tran_date
-- 				   upload_date = ls_nr_date 
					-- modify by lch 20210630 end 
-- 					and tran_type in ('sale', 'return')
					-- modify by ty 20210513 end
					to_char(tran_date::timestamp, 'HH24MISS')>='080000' 
					and to_char(tran_date::timestamp, 'HH24MISS')<'100000' 
			group by case when to_char(tran_date,'HH24miss')<'060000'
							  then to_char(tran_date+ '-1 day','yyyyMMdd')
						 else to_char(tran_date,'yyyyMMdd')
					end, 
					store_cd) b 
	where	a.acc_date = b.acc_date 
			and a.store_cd = b.store_cd
			and a.in_service = '10';
			
	get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
		
			------------------------------------------------
    -- STEP32: 更新Exclude Services "8h_10h" 字段
    ------------------------------------------------
	select usp_step_begin_log(step_no) into step_no;

	update	ia0040 a 
	set		"8h_10h" = b.sum
	from  	(select c.acc_date,c.store_cd,sum(c.sale_amount- c.over_amount) from
	(select	
					-- modify by ty 20210513 start
					-- acc_date,
					case when to_char(tran_date,'HH24miss')<'060000'
							  then to_char(tran_date+ '-1 day','yyyyMMdd')
						 else to_char(tran_date,'yyyyMMdd')
					end as acc_date,
					-- modify by ty 20210513 end, 
					a.store_cd, 
					-- modify by lch 20210628 start
-- 					sum(a.sale_amount-over_amount) 
						a.sale_amount,a.over_amount,a.pos_id,a.tran_serial_no
					-- modify by lch 20210628 end, 
			from	sa0010_tmp2 a, sa0020_tmp2 b 
			where		a.acc_date = b.acc_date 
						-- modify by lch 20210630 start
				-- and a.tran_date between ls_tran_date and ls_next_tran_date
-- 				and a.upload_date = ls_nr_date 
				-- modify by lch 20210630 end 
						and a.store_cd = b.store_cd 
						and a.pos_id = b.pos_id 
						and a.tran_serial_no = b.tran_serial_no
-- 						and a.tran_type in ('sale', 'return')
						-- modify by lch 20210628 start
-- 						and b.dep_cd != '03' 
						and b.article_id not in (SELECT article_id from to_hq_tmp.service_sales ss)
						-- modify by lch 20210628 end,
						and to_char(a.tran_date::timestamp, 'HH24MISS')>='080000' 
					and to_char(a.tran_date::timestamp, 'HH24MISS')<'100000' 
			group by 
					case when to_char(a.tran_date,'HH24miss')<'060000'
	                      then to_char(a.tran_date+ '-1 day','yyyyMMdd')
					else to_char(a.tran_date,'yyyyMMdd')
					end, 
					-- add by lch 20210628 start
					a.sale_amount,a.over_amount,a.pos_id,a.tran_serial_no,
					-- add by lch 20210628 end
					a.store_cd) c GROUP BY c.acc_date,c.store_cd ) b 
	where	a.acc_date = b.acc_date 
			and a.store_cd = b.store_cd
			and a.in_service = '20';

	get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
	
	------------------------------------------------
    -- STEP33: 更新Include Services "10h_12h" 字段
    ------------------------------------------------
	select usp_step_begin_log(step_no) into step_no;
	
	update	ia0040 a 
	set		"10h_12h" = b.sum 
	from  	(select	-- modify by ty 20210513 start
					-- acc_date,
					case when to_char(tran_date,'HH24miss')<'060000'
							  then to_char(tran_date+ '-1 day','yyyyMMdd')
						 else to_char(tran_date,'yyyyMMdd')
					end as acc_date,
					-- modify by ty 20210513 end, 
					store_cd, 
					sum(sale_amount-over_amount) 
			from	sa0010_tmp2 
			where	-- modify by ty 20210513 start 
					-- acc_date = ls_nr_date 
					-- modify by lch 20210630 start
					-- tran_date between ls_tran_date and ls_next_tran_date
-- 				   upload_date = ls_nr_date 
					-- modify by lch 20210630 end 
-- 					and tran_type in ('sale', 'return')
					-- modify by ty 20210513 end
					to_char(tran_date::timestamp, 'HH24MISS')>='100000' 
					and to_char(tran_date::timestamp, 'HH24MISS')<'120000' 
			group by case when to_char(tran_date,'HH24miss')<'060000'
							  then to_char(tran_date+ '-1 day','yyyyMMdd')
						 else to_char(tran_date,'yyyyMMdd')
					end, 
					store_cd) b 
	where	a.acc_date = b.acc_date 
			and a.store_cd = b.store_cd
			and a.in_service = '10';
	
	get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
		
			------------------------------------------------
    -- STEP34: 更新Exclude Services "10h_12h" 字段
    ------------------------------------------------
	select usp_step_begin_log(step_no) into step_no;

	update	ia0040 a 
	set		"10h_12h" = b.sum
	from  	(select c.acc_date,c.store_cd,sum(c.sale_amount- c.over_amount) from
	(select	
					-- modify by ty 20210513 start
					-- acc_date,
					case when to_char(tran_date,'HH24miss')<'060000'
							  then to_char(tran_date+ '-1 day','yyyyMMdd')
						 else to_char(tran_date,'yyyyMMdd')
					end as acc_date,
					-- modify by ty 20210513 end, 
					a.store_cd, 
					-- modify by lch 20210628 start
-- 					sum(a.sale_amount-over_amount) 
						a.sale_amount,a.over_amount,a.pos_id,a.tran_serial_no
					-- modify by lch 20210628 end, 
			from	sa0010_tmp2 a, sa0020_tmp2 b 
			where		a.acc_date = b.acc_date 
						-- modify by lch 20210630 start
				-- and a.tran_date between ls_tran_date and ls_next_tran_date
-- 				and a.upload_date = ls_nr_date 
				-- modify by lch 20210630 end 	
						and a.store_cd = b.store_cd 
						and a.pos_id = b.pos_id 
						and a.tran_serial_no = b.tran_serial_no
-- 						and a.tran_type in ('sale', 'return')
						-- modify by lch 20210628 start
-- 						and b.dep_cd != '03' 
						and b.article_id not in (SELECT article_id from to_hq_tmp.service_sales ss)
						-- modify by lch 20210628 end,
						and to_char(a.tran_date::timestamp, 'HH24MISS')>='100000' 
					and to_char(a.tran_date::timestamp, 'HH24MISS')<'120000' 
			group by 
					case when to_char(a.tran_date,'HH24miss')<'060000'
	                      then to_char(a.tran_date+ '-1 day','yyyyMMdd')
					else to_char(a.tran_date,'yyyyMMdd')
					end, 
					-- add by lch 20210628 start
					a.sale_amount,a.over_amount,a.pos_id,a.tran_serial_no,
					-- add by lch 20210628 end
					a.store_cd) c GROUP BY c.acc_date,c.store_cd ) b  
	where	a.acc_date = b.acc_date 
			and a.store_cd = b.store_cd
			and a.in_service = '20';

	get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
	
	------------------------------------------------
    -- STEP35: 更新Include Services shift1 字段
    ------------------------------------------------
	select usp_step_begin_log(step_no) into step_no;
	
	update	ia0040 
	set		shift1 = "6h_8h" + "8h_10h" + "10h_12h" + "12h_14h";
	
	get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
	
	------------------------------------------------
    -- STEP36: 更新 shift2 字段
    ------------------------------------------------
	select usp_step_begin_log(step_no) into step_no;
	
	update	ia0040 a 
	set		shift2 = "14h_16h" + "16h_18h" + "18h_20h" + "20h_22h";
	
	get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
	
	------------------------------------------------
    -- STEP37: 更新 shift3 字段
    ------------------------------------------------
	select usp_step_begin_log(step_no) into step_no;
	
	update	ia0040 a 
	set		shift3 = "22h_24h" + "0h_2h" + "2h_4h" + "4h_6h";
	
	get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
		
		--------------------------------------
    -- STEP38:清除临时表数据
    --------------------------------------
    select usp_step_begin_log(step_no) into step_no;
		
    truncate sa0010_tmp2;
		
    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
	
	--------------------------------------
    -- STEP39:清除临时表数据
    --------------------------------------
    select usp_step_begin_log(step_no) into step_no;
		
    truncate sa0020_tmp2;
		
    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
		
    --------------------------------------
    -- 出口处理
    --------------------------------------
    perform usp_proc_end_log('usp_create_ia0040',0);
    exception
      when others then
        get stacked diagnostics rtn_code :=RETURNED_SQLSTATE,
                                msg :=MESSAGE_TEXT;
        raise notice 'error_code=% , %',rtn_code,msg;
        perform usp_proc_end_log('usp_create_ia0040',1);
end;
$BODY$;
