-- PROCEDURE: public.usp_create_sales_by_hht()

-- DROP PROCEDURE public.usp_create_sales_by_hht();

CREATE OR REPLACE PROCEDURE public.usp_create_sales_by_hht(
	)
LANGUAGE 'plpgsql'
AS $BODY$
declare
    -----------------------------------------------------------------------------------------
    -- 变量定义
    -----------------------------------------------------------------------------------------
    step_no int;                                --步骤编号
    rtn_rows int;                               --SQL语句执行行数
    rtn_code text;                              --SQL语句返回值
    para_value varchar(100);                    --JOB参数显示用字符串
    sale_no int;                								--当天的最大sale_serial_no

	msg text;

begin
    ----------------------------------------------------------------------------------------
    -- 变量初始化
    ----------------------------------------------------------------------------------------
    step_no := 0;
    rtn_code := 0;
	para_value := '';
	sale_no := 0;

    ------------------------------------------------
    -- JOB开始
    ------------------------------------------------
    perform usp_proc_begin_log('usp_create_sales_by_hht',para_value);
	
    ------------------------------------------------
    -- STEP1:将表selling_upload_header按transdate排序，并将该列命名为rownum，创建临时表并将结果保存到该表中
    ------------------------------------------------
    select usp_step_begin_log(step_no) into step_no;

	create table tmp_table as (select a.*, (row_number() over(order by transdate)
	+(SELECT coalesce(max(sa0010.sale_serial_no),0) from sa0010 where sa0010.tran_date >= TO_TIMESTAMP(to_char(now(),'yyyy-MM-dd')||' 06:00:00', 'YYYY-MM-DD HH24:MI:SS') 	and (sa0010.tran_date - INTERVAL'1 day') < TO_TIMESTAMP(to_char(now(),'yyyy-MM-dd')||' 06:00:00', 'YYYY-MM-DD HH24:MI:SS')))
	as rownum from hht_from_hcmc.selling_upload_header a); 

    
    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
		
    
    ------------------------------------------------
    -- STEP2:生成sa0010 hht销售头档
    ------------------------------------------------
    select usp_step_begin_log(step_no) into step_no;

    insert into public.sa0010(
				acc_date,
				store_cd,
				pos_id,
                tran_serial_no, 
                tran_date, 
                sale_serial_no, 
                tran_type, 
                sale_type, 
                void_pos_id, 
                void_sale_serial_no, 
                cashier_id, 
                member_point, 
                shift_serial_no, 
                z_serial_no, 
                invoice_code, 
                invoice_serial_no,
				invoice_cnt,
				age_level_cd,
				detail_cnt,
				gross_sale_amount,
				discount_amount,
				sale_amount,
				over_amount,
				pay_cd1,
				pay_cd2,
				pay_cd3,
				pay_cd4,
				pay_amount1,
				pay_amount2,
				pay_amount3,
				pay_amount4,
				credit_card_number1,
				credit_card_number2,
				credit_card_number3,
				credit_card_number4,
				check_id1,
				check_id2,
				check_id3,
				check_id4,
				change_amount,
				spill_amount,
				upload_flg,
				upload_date,
				sc_upload_flg,
				sc_upload_date,
				create_user_id,
				create_ymd,
				create_hms,
				update_user_id,
				update_ymd,
				update_hms,
				update_screen_id,
				update_ip_address,
				nr_update_flg,
				nr_ymd,
				nr_hms,
				upload_stream_flg,
				in_es_time,
				staff_id,
				staff_disc_rate
		)
	select  distinct 
                to_char(a.transdate,'yyyyMMdd'), 
                a.storecode, 
				'99', /*pos_id*/
				cast(a.recptno as integer),
				-- a.transdate,
				(to_char(a.transdate,'yyyymmdd')||' '||a.transtime)::timestamp as transdate,
				tmp.rownum,
				(case a.mode when 'S' then 'sale' when 'R' then 'return' when 'V' then 'void' end), /*tran_type*/
				'0',
				null,
				0,
				'hht',
				0, /*member_point */
				0, 
				0,
				null,
				null,
				0,/*invoice_cnt */
				null, 
				0,/*detail_cnt */
				0.00,
				a.discamt, /*discount_amount*/
				a.totamt, /*sale_amount*/
				0.00, /*over_amount*/
				'01', /*pay_cd1*/
				null, /*pay_cd2*/
				null, /*pay_cd3*/
				null, /*pay_cd4*/
				a.payamt, /*pay_amount1*/
				0.00, /*pay_amount2*/
				0.00, /*pay_amount3*/
				0.00, /*pay_amount4*/
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,/*check_id4*/
				coalesce(a.payamt,0.00)-coalesce(a.totamt,0.00), /*change_amount*/
				0.00, /*spill_amount*/
				'1', /*upload_flg*/
				to_char(now(),'yyyyMMdd'), /*upload_date*/
				null,--'1', /*sc_upload_flg*/
				null,--to_char(now(),'yyyyMMdd'), /*sc_upload_date*/
				null,
				null,
				null,
				null,
				null,
				null,
				to_char(now(),'yyyyMMdd'), /* update_screen_id */
				null,
				null,
				null,
				null,
				'0', /* upload_stream_flg */
				floor(extract(epoch from now())), /*in_es_time*/
				null,
				null
    from    hht_from_hcmc.selling_upload_header a,tmp_table tmp
    where 
		a.storecode = tmp.storecode
		and a.repdate = tmp.repdate
		and a.billno = tmp.billno
		-- order by a.transdate;
		order by transdate;
            
    
    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
    
    
    
    
    ------------------------------------------------
    -- STEP3:生成 sa0020 hht销售明细商品
    ------------------------------------------------
    select usp_step_begin_log(step_no) into step_no;

    insert into public.sa0020(
                acc_date,
				store_cd,
				pos_id,
				tran_serial_no,
				sale_date,
				sale_serial_no,
				sale_seq_no,
				detail_type,
				detail_void,
				pma_cd,
				category_cd,
				sub_category_cd,
			    article_id,
				barcode,
				price_original,
				price_actual,
				sale_qty,
				sale_amount,
				discount_amount,
				upload_flg,
				upload_date,
				create_user_id,
				create_ymd,
				create_hms,
				update_user_id,
				update_ymd,
				update_hms,
				update_screen_id,
				update_ip_address,
				nr_update_flg,
				nr_ymd,
				nr_hms,
				upload_stream_flg,
				in_es_time,
				taste_memo,
				other_info,
				dep_cd) 
    select      
				to_char(c.transdate,'yyyyMMdd') , 
				a.storecode, 
				'99', /*pos_id*/
				cast(c.recptno as integer), /*tran_serial_no*/
				-- a.repdate, /*sale_date*/
				(to_char(a.repdate,'yyyymmdd')||' '||c.transtime)::timestamp as transdate,
				  tmp.rownum, /*sale_serial_no*/
				a.linenum, /*sale_seq_no*/
				(case c.mode when 'S' then 's' when 'R' then 'r' when 'V' then 'v' end), /*detail_type*/
				'0', /*detail_void*/
				b.pma_cd,
				b.category_cd,
				b.sub_category_cd,
                a.itemcode, 
                a.barcode, 
				a.price,--0.00, /*price_original*/
				a.price, /*price_actual*/
				a.qty, /*sale_qty*/
				a.amount, /*sale_amount*/
				a.discamt, /*discount_amount*/
				null,--'1', /*upload_flg*/
				null,--to_char(now(),'yyyyMMdd'), /*upload_date*/
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				'5', /* upload_stream_flg */
				floor(extract(epoch from now())), /*in_es_time*/
				null,
				null,
				b.dep_cd
    from        hht_from_hcmc.selling_upload_detail a ,
                public.ma1100 b,
				hht_from_hcmc.selling_upload_header c,
				tmp_table tmp
    where                 			
				a.itemcode = b.article_id 
                and to_char(a.repdate,'yyyyMMdd') between b.effective_start_date and b.effective_end_date
				and a.storecode = c.storecode 
				and a.repdate = c.repdate
				and a.billno = c.billno
				and a.storecode = tmp.storecode
				and a.repdate = tmp.repdate
				and a.billno = tmp.billno
				-- order by c.transdate;
	order by transdate;
    
    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
    
    ------------------------------------------------
    -- STEP4:生成 sa0580 hht noSale商品
    ------------------------------------------------
    select usp_step_begin_log(step_no) into step_no;

    insert into public.sa0580(
                acc_date, 
                article_id, 
                cashier_id, 
				in_es_time,
                pos_id, 
                qty, 
                sale_type, 
                shift_name, 
                shift_serial_no, 
                store_cd, 
                taste_no, 
                tran_date, 
                tran_serial_no,
                tran_type, 
                upload_flg, 
                z_serial_no) 
    select      to_char(a.repdate,'yyyyMMdd') , 
                a.itemcode, 
                null, /*cashier_id*/
                floor(extract(epoch from now())), /*in_es_time*/
                '99', /*pos_id*/ 
				a.qty,
				'0', /*sale_type*/ 
				(case when '060000' <= a.transtime and a.transtime < '140000'   then 'MorningShift'
				 when '140000' <= a.transtime and a.transtime < '220000'  then 'MiddleShift' else 'NightShift' end), /*shift_name*/ 
				0, /*shift_serial_no*/ 
				a.storecode, 
				'', /*taste_no*/ 
				(to_char(a.transdate,'yyyymmdd')||' '||a.transtime)::timestamp , -- a.repdate, modify by ty 20210825
				cast(a.recptno as integer), /*tran_serial_no*/ 
				'sale', /*tran_type*/ 
				'1', /*upload_flg*/ 
				0 /*z_serial_no*/ 
    from        hht_from_hcmc.selling_upload_nosale a;
	
    
    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
		
	------------------------------------------------
    -- STEP5:更新 sa0580 hht shift_serial_no
    ------------------------------------------------
		update sa0580 set shift_serial_no = a.rownum from 
		(select a.*,row_number() over(partition by a.shift_name order by a.tran_date) as rownum
		from sa0580 a ) a 
		where a.acc_date = sa0580.acc_date
		and a.store_cd = sa0580.store_cd
		and a.pos_id = sa0580.pos_id
		and a.tran_serial_no = sa0580.tran_serial_no
		and a.article_id = sa0580.article_id
		and a.tran_date = sa0580.tran_date;
		
	get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
		
		
	------------------------------------------------
    -- STEP6: 删除临时表
    ------------------------------------------------
    select usp_step_begin_log(step_no) into step_no;
		 
	drop table if exists tmp_table;
		
	get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
		
    ------------------------------------------------
    -- 出口处理
    ------------------------------------------------
    perform usp_proc_end_log('usp_create_sales_by_hht',0);
    exception
      when others then
        get stacked diagnostics rtn_code :=RETURNED_SQLSTATE,
                                msg :=MESSAGE_TEXT;
        raise notice 'error_code=% , %',rtn_code,msg;
        perform usp_proc_end_log('usp_create_sales_by_hht',1);
end;
$BODY$;
