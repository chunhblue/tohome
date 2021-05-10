CREATE OR REPLACE FUNCTION "public"."usp_import_hq_to_st2"()
  RETURNS "pg_catalog"."void" AS $BODY$
declare
    -----------------------------------------------------------------------------------------
    -- 变量定义
    -----------------------------------------------------------------------------------------
    step_no int;                                --步骤编号
    rtn_rows int;                               --SQL语句执行行数
    rtn_code text;                              --错误代码
    msg text;                                   --错误信息
    para_value varchar(100);                    --JOB参数显示用字符串
	ls_nr_date varchar(8);						-- 业务日期
    path text;                         		    --导入路径 
begin
    ----------------------------------------------------------------------------------------
    -- 变量初始化
    ----------------------------------------------------------------------------------------
    step_no := 0;
    rtn_code := 0;
	--  path := '/data/hadoop/data/store/from_hq2/';
    path := '/tmp/20201230/';

    --------------------------------------
    -- JOB开始
    --------------------------------------
    perform usp_proc_begin_log('usp_import_hq_to_st2',para_value);
    --------------------------------------
    -- STEP1:取得日结日期
    --------------------------------------
    select usp_step_begin_log(step_no) into step_no;

	select sp_value into ls_nr_date from cm9060 where sp_id = '0006';
    --  path:=path||ls_nr_date||'/';
    
    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
	
    --------------------------------------
    -- STEP2:清除表 sk0000_today 数据
    --------------------------------------
    select usp_step_begin_log(step_no) into step_no;
	
    truncate table sk0000_today;
	
    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
    
	--------------------------------------
    -- STEP3:导入表 sk0000_today
    --------------------------------------
    select usp_step_begin_log(step_no) into step_no;
	
    execute 'copy sk0000_today from '''||''||path||'sk0000_today.csv''';

    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
	
	--------------------------------------
    -- STEP4:清除表 sk0000 数据
    --------------------------------------
    select usp_step_begin_log(step_no) into step_no;
	
	delete from sk0000 where effective_date = to_char(to_date(ls_nr_date, 'yyyymmdd')+1, 'yyyymmdd');

    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
    
	--------------------------------------
    -- STEP5:导入表 sk0000
    --------------------------------------
    select usp_step_begin_log(step_no) into step_no;
	
    execute 'copy sk0000 from '''||''||path||'sk0000.csv''';

    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
	
    --  add by dxq 20210208 start 
    --------------------------------------
    -- STEP6:清除表 ia0000 数据
    --------------------------------------
    select usp_step_begin_log(step_no) into step_no;
	
    truncate table  ia0000 ;--where acc_date = ls_nr_date;

    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
    
	--------------------------------------
    -- STEP7:导入表 ia0000
    --------------------------------------
    select usp_step_begin_log(step_no) into step_no;
	
    execute 'copy ia0000 from '''||''||path||'ia0000.csv''';

    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
    --  add by dxq 20210208 end 
    
	--------------------------------------
    -- STEP8:清除表 ia0010 数据
    --------------------------------------
    select usp_step_begin_log(step_no) into step_no;
	
    truncate table ia0010;--where acc_date = ls_nr_date;

    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
    
	--------------------------------------
    -- STEP9:导入表 ia0010
    --------------------------------------
    select usp_step_begin_log(step_no) into step_no;
	
    execute 'copy ia0010 from '''||''||path||'ia0010.csv''';

    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
	
	--------------------------------------
    -- STEP10:清除表 ia0020 数据
    --------------------------------------
    select usp_step_begin_log(step_no) into step_no;
	
    truncate table ia0020;-- where acc_date = ls_nr_date;

    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
    
	--------------------------------------
    -- STEP11:导入表 ia0020
    --------------------------------------
    select usp_step_begin_log(step_no) into step_no;
	
    execute 'copy ia0020 from '''||''||path||'ia0020.csv''';

    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
	
	--------------------------------------
    -- STEP12:清除表 sk3000 数据
    --------------------------------------
    select usp_step_begin_log(step_no) into step_no;
	
    truncate table sk3000 ;--where acc_date = ls_nr_date;

    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
    
	--------------------------------------
    -- STEP13:导入表 sk3000
    --------------------------------------
    select usp_step_begin_log(step_no) into step_no;
	
    execute 'copy sk3000 from '''||''||path||'sk3000.csv''';

    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
	
	--------------------------------------
    -- STEP14:清除表 ma1220 数据
    --------------------------------------
    select usp_step_begin_log(step_no) into step_no;
	
	delete from ma1220 where unpack_date = ls_nr_date;

    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
    
	--------------------------------------
    -- STEP15:导入表 ma1220
    --------------------------------------
    select usp_step_begin_log(step_no) into step_no;
	
    execute 'copy ma1220 from '''||''||path||'w_ma1220_t.csv''';

    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
	
	--------------------------------------
    -- STEP16:清除表 ma1230 数据
    --------------------------------------
    select usp_step_begin_log(step_no) into step_no;
	
	delete from ma1230 where unpack_id not in (select unpack_id from ma1220) or unpack_id in (select unpack_id from ma1220 where unpack_date = ls_nr_date);

    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
    
	--------------------------------------
    -- STEP17:导入表 ma1230
    --------------------------------------
    select usp_step_begin_log(step_no) into step_no;
	
    execute 'copy ma1230 from '''||''||path||'w_ma1230_t.csv''';

    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
	
	--------------------------------------
    -- STEP18:清除表 ma4360 数据
    --------------------------------------
    select usp_step_begin_log(step_no) into step_no;
	
	delete from ma4360 where voucher_cd in (select voucher_cd from ma4350 where sale_date = ls_nr_date);

    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
    
	--------------------------------------
    -- STEP19:导入表 ma4360
    --------------------------------------
    select usp_step_begin_log(step_no) into step_no;
	
    execute 'copy ma4360 from '''||''||path||'w_ma4360_t.csv''';

    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
	
	--------------------------------------
    -- STEP20:清除表 ma4350 数据
    --------------------------------------
    select usp_step_begin_log(step_no) into step_no;
	
	delete from ma4350 where sale_date = ls_nr_date;

    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
    
	--------------------------------------
    -- STEP21:导入表 ma4350
    --------------------------------------
    select usp_step_begin_log(step_no) into step_no;
	
    execute 'copy ma4350 from '''||''||path||'w_ma4350_t.csv''';

    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
	
	--------------------------------------
    -- STEP22:清除表 t_sa0210_01 数据
    --------------------------------------
    select usp_step_begin_log(step_no) into step_no;
	
    --delete from t_sa0210_01 where acc_date = ls_nr_date;

    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
    
	--------------------------------------
    -- STEP23:导入表 t_sa0210_01
    --------------------------------------
    select usp_step_begin_log(step_no) into step_no;
	
    --execute 'copy t_sa0210_01 from '''||''||path||'sa0210.csv''';

    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
    
	--------------------------------------
	-- 出口处理
	--------------------------------------
	perform usp_proc_end_log('usp_import_hq_to_st2',0);
	exception
	  when others then
		get stacked diagnostics rtn_code :=RETURNED_SQLSTATE,
								msg :=MESSAGE_TEXT;
		raise notice 'error_code=% , %',rtn_code,msg;
		perform usp_proc_end_log('usp_import_hq_to_st2',1);
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100