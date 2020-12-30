CREATE INDEX i_iyed_tax_receipt ON iyed_tax_receipt (vendor,tax_po_no,tax_po_type,company)
TABLESPACE  MD_WEEKLY_IDX
STORAGE( INITIAL    30M    NEXT 500K
	 MINEXTENTS 1       MAXEXTENTS 99
	 PCTINCREASE 0      );
         COMMIT;
         EXIT;
