DELETE from ma8250;


INSERT into ma8250
select
  to_char(CURRENT_DATE,'yyyymmdd') acc_date,
	structure_cd,
  max(case when acc_date=to_char(CURRENT_DATE::TIMESTAMP -INTERVAL'7 day','yyyymmdd') then pollution_level else null end) as aqi7b,
  max(case when acc_date=to_char(CURRENT_DATE::TIMESTAMP -INTERVAL'6 day','yyyymmdd') then pollution_level else null end) as aqi6b,
  max(case when acc_date=to_char(CURRENT_DATE::TIMESTAMP -INTERVAL'5 day','yyyymmdd') then pollution_level else null end) as aqi5b,
  max(case when acc_date=to_char(CURRENT_DATE::TIMESTAMP -INTERVAL'4 day','yyyymmdd') then pollution_level else null end) as aqi4b,
  max(case when acc_date=to_char(CURRENT_DATE::TIMESTAMP -INTERVAL'3 day','yyyymmdd') then pollution_level else null end) as aqi3b,
  max(case when acc_date=to_char(CURRENT_DATE::TIMESTAMP -INTERVAL'2 day','yyyymmdd') then pollution_level else null end) as aqi2b,
  max(case when acc_date=to_char(CURRENT_DATE::TIMESTAMP -INTERVAL'1 day','yyyymmdd') then pollution_level else null end) as aqi1b,
  max(case when acc_date=to_char(CURRENT_DATE,'yyyymmdd') then pollution_level else null end) as aqi,
  max(case when acc_date=to_char(CURRENT_DATE::TIMESTAMP +INTERVAL'1 day','yyyymmdd') then pollution_level else null end) as aqi1a,
  max(case when acc_date=to_char(CURRENT_DATE::TIMESTAMP +INTERVAL'2 day','yyyymmdd') then pollution_level else null end) as aqi2a,
  max(case when acc_date=to_char(CURRENT_DATE::TIMESTAMP +INTERVAL'3 day','yyyymmdd') then pollution_level else null end) as aqi3a,
  max(case when acc_date=to_char(CURRENT_DATE::TIMESTAMP +INTERVAL'4 day','yyyymmdd') then pollution_level else null end) as aqi4a,
  max(case when acc_date=to_char(CURRENT_DATE::TIMESTAMP +INTERVAL'5 day','yyyymmdd') then pollution_level else null end) as aqi5a,
  max(case when acc_date=to_char(CURRENT_DATE::TIMESTAMP +INTERVAL'6 day','yyyymmdd') then pollution_level else null end) as aqi6a,
  max(case when acc_date=to_char(CURRENT_DATE::TIMESTAMP +INTERVAL'7 day','yyyymmdd') then pollution_level else null end) as aqi7a
from ma8260 group by structure_cd;