-- PROCEDURE: sy.usp_create_article_price_label()

-- DROP PROCEDURE sy.usp_create_article_price_label();

CREATE OR REPLACE PROCEDURE sy.usp_create_article_price_label(
	)
LANGUAGE 'plpgsql'
AS $BODY$
declare
    -----------------------------------------------------------------------------------------
    -- 变量定义
    -----------------------------------------------------------------------------------------
    step_no int;                                --步骤编号
    rtn_rows int;                               --SQL语句执行行数
    rtn_code text;                             --SQL语句返回值
    para_value varchar(100);                   --JOB参数显示用字符串
    ls_nr_date char(8);                        --日结日期
    ls_next_3_day_date char(8);                --接下来3天的日期

	msg text;

begin
    ----------------------------------------------------------------------------------------
    -- 变量初始化
    ----------------------------------------------------------------------------------------
    step_no := 0;
    rtn_code := 0;
	para_value := '';

    ------------------------------------------------
    -- JOB开始
    ------------------------------------------------
    perform usp_proc_begin_log('usp_create_article_price_label',para_value);
	
    ------------------------------------------------
    -- STEP1:获取日结日期
    ------------------------------------------------
    select usp_step_begin_log(step_no) into step_no;

	select usp_get_store_nr_date() into ls_nr_date;
    
    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
	
    ------------------------------------------------
    -- STEP2:计算接下来3天的日期
    ------------------------------------------------
    select usp_step_begin_log(step_no) into step_no;

	select to_char(ls_nr_date::date + 3, 'yyyymmdd') into ls_next_3_day_date;
    
    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
    
    ------------------------------------------------
    -- STEP3:清除表数据
    ------------------------------------------------
    select usp_step_begin_log(step_no) into step_no;

	truncate table ma8300;
    truncate table t_ma8300_01;
    
    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
    
    ------------------------------------------------
    -- STEP4:获取接下来几天生效的商品信息
    ------------------------------------------------
    select usp_step_begin_log(step_no) into step_no;

    insert into sy.t_ma8300_01(
                article_id, 
                article_name, 
                barcode, 
                effective_start_date, 
                name_en_price_label, 
                name_vn_price_label, 
                dep_cd, 
                pma_cd, 
                category_cd, 
                sub_category_cd, 
                spec, 
                sales_unit_id, 
                structure_cd)
	select      distinct 
                a.article_id, 
                a.article_name_vietnamese, 
                b.default_barcode, 
                a.effective_start_date, 
                a.name_en_price_label, 
                a.name_vn_price_label, 
                a.dep_cd, 
                a.pma_cd, 
                a.category_cd, 
                a.sub_category_cd, 
                a.spec, 
                a.sales_unit_id, 
                b.structure_cd 
    from    ma1100 a, ma1110 b 
    where   a.article_id = b.article_id 
            and a.effective_start_date = b.effective_start_date 
            and a.effective_start_date > ls_nr_date 
            and a.effective_start_date <= ls_next_3_day_date 
            and a.review_status = 10 
            and exists (select  1 
                        from    hq.ma1130 c 
                        where   a.article_id = c.article_id 
                                and a.effective_start_date = c.effective_start_date 
                                and b.article_id = c.article_id 
                                and b.effective_start_date = c.effective_start_date 
                                and b.structure_cd = c.structure_cd 
                                and COALESCE(c.sale_inactive, '0') <> '1');
    
    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
    
    ------------------------------------------------
    -- STEP5:更新上一条履历的开始日期
    ------------------------------------------------
    select usp_step_begin_log(step_no) into step_no;

    update  sy.t_ma8300_01 a 
    set     old_effective_start_date = b.old_effective_start_date 
    from    (select c.article_id, 
                    c.barcode, 
                    c.effective_start_date, 
                    c.structure_cd, 
                    max(d.effective_start_date) as old_effective_start_date 
            from    sy.t_ma8300_01 c, hq.ma1130 d 
            where   c.article_id = d.article_id 
                    and c.effective_start_date > d.effective_start_date 
                    and c.structure_cd = d.structure_cd 
            group by c.article_id, 
                    c.barcode, 
                    c.effective_start_date, 
                    c.structure_cd) b 
    where   a.article_id = b.article_id 
            and a.barcode = b.barcode 
            and a.effective_start_date = b.effective_start_date 
            and a.structure_cd = b.structure_cd;
    
    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
    
    ------------------------------------------------
    -- STEP6:生成 ma8300 新品
    ------------------------------------------------
    select usp_step_begin_log(step_no) into step_no;

    insert into sy.ma8300(
                store_cd, 
                article_id, 
                article_name, 
                barcode, 
                effective_start_date, 
                name_en_price_label, 
                name_vn_price_label, 
                dep_cd, 
                pma_cd, 
                category_cd, 
                sub_category_cd, 
                spec, 
                sales_unit_id, 
                old_price, 
                new_price) 
    select      b.store_cd, 
                a.article_id, 
                a.article_name, 
                a.barcode, 
                a.effective_start_date, 
                a.name_en_price_label, 
                a.name_vn_price_label, 
                a.dep_cd, 
                a.pma_cd, 
                a.category_cd, 
                a.sub_category_cd, 
                a.spec, 
                a.sales_unit_id, 
                null, 
                c.base_sale_price 
    from        sy.t_ma8300_01 a, 
                ma1000_t b, 
                ma1130 c 
    where       a.article_id = c.article_id 
                and a.effective_start_date = c.effective_start_date 
				and a.structure_cd = c.structure_cd 
                and b.zo_cd = c.structure_cd 
                and b.ma_cd = c.ma_cd 
                and trim(coalesce(a.old_effective_start_date, '')) = '';
    
    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
    
    ------------------------------------------------
    -- STEP7:生成 ma8300 非新品
    ------------------------------------------------
    select usp_step_begin_log(step_no) into step_no;

    insert into sy.ma8300(
                store_cd, 
                article_id, 
                article_name, 
                barcode, 
                effective_start_date, 
                name_en_price_label, 
                name_vn_price_label, 
                dep_cd, 
                pma_cd, 
                category_cd, 
                sub_category_cd, 
                spec, 
                sales_unit_id, 
                old_price, 
                new_price) 
    select      b.store_cd, 
                a.article_id, 
                a.article_name, 
                a.barcode, 
                a.effective_start_date, 
                a.name_en_price_label, 
                a.name_vn_price_label, 
                a.dep_cd, 
                a.pma_cd, 
                a.category_cd, 
                a.sub_category_cd, 
                a.spec, 
                a.sales_unit_id, 
                d.base_sale_price, 
                c.base_sale_price 
    from        sy.t_ma8300_01 a, 
                ma1000_t b, 
                ma1130 c, 
                ma1130 d 
    where       a.article_id = c.article_id 
                and a.effective_start_date = c.effective_start_date 
				and a.structure_cd = c.structure_cd 
                and a.article_id = d.article_id 
                and a.old_effective_start_date = d.effective_start_date 
                and a.structure_cd = d.structure_cd 
                and b.zo_cd = c.structure_cd 
                and b.ma_cd = c.ma_cd 
                and trim(coalesce(a.old_effective_start_date, '')) <> '' 
                and d.base_sale_price <> c.base_sale_price;
    
    get diagnostics rtn_rows =ROW_COUNT;
    select usp_step_end_log(step_no,rtn_rows) into step_no;
    
    ------------------------------------------------
    -- 出口处理
    ------------------------------------------------
    perform usp_proc_end_log('usp_create_article_price_label',0);
    exception
      when others then
        get stacked diagnostics rtn_code :=RETURNED_SQLSTATE,
                                msg :=MESSAGE_TEXT;
        raise notice 'error_code=% , %',rtn_code,msg;
        perform usp_proc_end_log('usp_create_article_price_label',1);
end;
$BODY$;

COMMENT ON PROCEDURE sy.usp_create_article_price_label()
    IS '商品价格标签';
