package sud.registr;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import sud.enums.DirectionType;
import sud.enums.HistoryStatusType;

import java.util.List;

@RequiredArgsConstructor
@Log4j2
@Service
public class RegistrService {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<LawsuitRegistrRec> findAllLawsuit() {
        return findAllLawsuit(null);
    }

    public List<LawsuitRegistrRec> findAllLawsuit(String lsNumber) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("FilterLsNumber", lsNumber);
        namedParameters.addValue("FilterCanceled", HistoryStatusType.CANCELED.getCode());
        namedParameters.addValue("FilterFinished", HistoryStatusType.FINISHED.getCode());
        return jdbcTemplate.query("""                               
              
                select *
                                   from (select l.id,
                                                ls_description,
                                                ls_name,
                                                ls_number,
                                                l.reg_number                                                                      lsRegNum,
                                                c.short_name                                                                      short_name,
                                                c.full_name                                                                       full_name,
                                                c.site                                                                            site,
                                                c.tel_number                                                                      tel_number,
                                                c.address                                                                         address,
                                                (select max(cs.begin_date_time) from court_session cs where cs.lawsuit_id = l.id) court_session_date,
                                                za.name                                                                           za_name,
                                                trim(za.surname || ' ' || za.first_name || ' ' || za.patronymic)                  za_fio,
                                                za.phone                                                                          za_phone,
                                                za.email                                                                          za_email,
                                                za.address                                                                        za_address,
                                                otv.name                                                                          otv_name,
                                                trim(otv.surname || ' ' || otv.first_name || ' ' || otv.patronymic)               otv_fio,
                                                otv.phone                                                                         otv_phone,
                                                otv.email                                                                         otv_email,
                                                otv.address                                                                       otv_address,
                                                (select hs.code
                                                 from history_status hs
                                                 where hs.start_date_time = hss1.start_date
                                                   and hs.lawsuit_id = l.id)                                                      status_code,
                                                (select hs.start_date_time
                                                 from history_status hs
                                                 where hs.start_date_time = hss1.start_date
                                                   and hs.lawsuit_id = l.id)                                                      status_date,
                                                pay_sum_in_sum,
                                                pay_sum_out_sum,
                                                COALESCE(pay_sum_in_sum, 0) - COALESCE(pay_sum_out_sum, 0)                        resultSum,
                                                l.court_id,
                                                trim(cp.surname || ' ' || cp.first_name || ' ' || cp.patronymic)                  court_person_fio,
                                                za.id                                                                             za_id,
                                                otv.id                                                                            otv_id,
                                                lpl1.client_flag                                                                  za_client_flag,
                                                lpl2.client_flag                                                                  otv_client_flag
                                         from lawsuit l
                                                  join court c on l.court_id = c.id
                                                  join lawsuit_person_link lpl1 on lpl1.lawsuit_id = l.id
                                                  join person za on (lpl1.person_id = za.id and za.direction = 1) --заявитель всегда присутствует
                                                  join lawsuit_person_link lpl2 on lpl2.lawsuit_id = l.id
                                                  join person otv on (lpl2.person_id = otv.id and otv.direction = 2) -- ответчик  всегда присутствует
                                                  left join (select sum(pmt.pay_sum_in)  pay_sum_in_sum,
                                                                    sum(pmt.pay_sum_out) pay_sum_out_sum,
                                                                    pmt.lawsuit_id
                                                             from payment pmt
                                                             group by pmt.lawsuit_id) s1 on s1.lawsuit_id = l.id
                                                  left join court_person_lawsuit_link cpll on cpll.lawsuit_id = l.id
                                                  left join person cp on cp.id = cpll.court_person_link_id
                                                  join (select max(hs1.start_date_time) start_date, hs1.lawsuit_id
                                                        from history_status hs1
                                                        where hs1.lawsuit_id not in (select hs2.lawsuit_id
                                                                                     from history_status hs2
                                                                                     where hs2.code in (:FilterCanceled, :FilterFinished)) -- в статусе завершено
                                                        group by hs1.lawsuit_id
                                                        union
                                                        -- в статусе завершено (на случай, если например было назначено судебное заседане на более позднюю дату, но дело закрыли
                                                        select max(hs3.start_date_time) start_date, hs3.lawsuit_id
                                                        from history_status hs3
                                                        where hs3.code in (:FilterCanceled, :FilterFinished) -- в статусе завершено
                                                        group by hs3.lawsuit_id) hss1 on hss1.lawsuit_id = l.id) as s_main
                                   where (s_main.ls_number = :FilterLsNumber OR :FilterLsNumber:: varchar IS NULL)
                 """, namedParameters, new DataClassRowMapper<>(LawsuitRegistrRec.class));
    }

    public List<IncomingDocRegistrRec> findIncomingDocsByLawsuitId(Long lawsuitId) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("FilterLawsuitId", lawsuitId);
        return jdbcTemplate.query("""  
                SELECT i.incoming_doc_id,
                       i.history_status_id,
                       i.id_author,
                       i.id_category,
                       i.id_doc_date,
                       i.id_doc_num,
                       i.id_name,
                       i.id_note,
                       i.has_original,
                       exists (select * from incoming_doc_file idf where i.incoming_doc_id = idf.incoming_doc_id) has_files
                FROM history_status hs join incoming_doc i on i.history_status_id = hs.id 
                WHERE hs.lawsuit_id = :FilterLawsuitId
                """, namedParameters, new DataClassRowMapper<>(IncomingDocRegistrRec.class));
    }

    public List<IncomingDocRegistrRec> findIncomingDocsByHistoryStatusId(Long historyStatusId) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("FilterLawsuitHistoryStatusId", historyStatusId);
        return jdbcTemplate.query("""  
                SELECT i.incoming_doc_id,
                       i.history_status_id,
                       i.id_author,
                       i.id_category,
                       i.id_doc_date,
                       i.id_doc_num,
                       i.id_name,
                       i.id_note,
                       i.has_original,
                       exists (select * from incoming_doc_file idf where i.incoming_doc_id = idf.incoming_doc_id) has_files
                FROM incoming_doc i
                WHERE i.history_status_id = :FilterLawsuitHistoryStatusId
                """, namedParameters, new DataClassRowMapper<>(IncomingDocRegistrRec.class));
    }

    public List<CourtSessionRegistrRec> findAllCourtSession(Long advacatPersonId) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("FilterAdvocatPersonId", advacatPersonId);
        return jdbcTemplate.query("""                               
                select * from (
                   select
                       cs.id court_session_id,
                       case when cs.begin_date_time > CURRENT_TIMESTAMP  then false else true end finished,
                       cs.begin_date_time,
                       cs.lawsuit_id,
                  (select
                      trim(za.name || ' ' || za.surname || ' ' || za.first_name || ' ' || za.patronymic)
                  from person za join lawsuit_person_link lp on lp.person_id = za.id
                  where (lp.lawsuit_id = cs.lawsuit_id and za.direction = 1)) za_name,
                 (select trim(otv.name || ' ' || otv.surname || ' ' || otv.first_name || ' ' || otv.patronymic)
                  from person otv join lawsuit_person_link lp on lp.person_id = otv.id
                  where (lp.lawsuit_id = cs.lawsuit_id and otv.direction = 2)) otv_name,
                       ls_name,
                       ls_number,
                       (select hs.code from history_status hs where hs.id = (
                           select max(hs2.id)
                           from history_status hs2
                           where hs2.lawsuit_id = l.id))  status_code,
                       c.short_name,
                       c.site,
                       (select trim(cp.surname || ' ' || cp.first_name || ' ' || cp.patronymic)
                        from person cp where cp.id = cpll.court_person_link_id) court_person_fio,
                        (select count(*) from court_session_person_link cspl
                                              where cspl.court_session_id = cs.id
                                              and (cspl.person_id = :FilterAdvocatPersonId or :FilterAdvocatPersonId::integer is null) ) advocat_count
                   from court_session cs
                   join lawsuit l on l.id = cs.lawsuit_id
                   left join court_person_lawsuit_link cpll on cpll.lawsuit_id = cs.lawsuit_id
                   left join court_person_link cpl on cpl.person_id = cpll.court_person_link_id
                   left join court c on cpl.court_id = c.id
                 ) as s_main
         
                    order by begin_date_time
                 """, namedParameters, new DataClassRowMapper<>(CourtSessionRegistrRec.class));
    }

    public List<PreparedLawsuitRegistrRec> findAllHistoryStatusLawsuit(Long advacatPersonId) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("FilterResponsePersonId", advacatPersonId);
        namedParameters.addValue("FilterCanceled", HistoryStatusType.CANCELED.getCode());
        namedParameters.addValue("FilterFinished", HistoryStatusType.FINISHED.getCode());
        return jdbcTemplate.query("""                               
             select hs.id,
                   case when hs.start_date_time > CURRENT_TIMESTAMP then false else true end                      finished,
                   hs.code,
                   hs.start_date_time,
                   hs.lawsuit_id,
                   (select trim(za.name || ' ' || za.surname || ' ' || za.first_name || ' ' || za.patronymic)
                    from person za
                             join lawsuit_person_link lp on lp.person_id = za.id
                    where (lp.lawsuit_id = hs.lawsuit_id and za.direction = 1))                                   za_name,
                   (select trim(otv.name || ' ' || otv.surname || ' ' || otv.first_name || ' ' || otv.patronymic)
                    from person otv
                             join lawsuit_person_link lp on lp.person_id = otv.id
                    where (lp.lawsuit_id = hs.lawsuit_id and otv.direction = 2))                                  otv_name,
                   ls_name,
                   ls_number,
                   (select count(*)
                    from history_status_person_link hspl
                    where hs.id = hspl.history_status_id
                      and (hspl.person_id = :FilterResponsePersonId or :FilterResponsePersonId::integer is null)) response_count
                            from
                history_status hs
                              join ( -- не в статусе завершено
                                     select max(hs1.start_date_time) start_date, hs1.lawsuit_id
                                     from history_status hs1
                                     where hs1.lawsuit_id not in (select hs2.lawsuit_id
                                                                  from history_status hs2
                                                                  where hs2.code in (:FilterCanceled, :FilterFinished)) -- в статусе завершено
                                     group by hs1.lawsuit_id
                                     union
                                     -- в статусе завершено (на случай, если например было назначено судебное заседане на более позднюю дату, но дело закрыли 
                                     select max(hs3.start_date_time) start_date, hs3.lawsuit_id
                                     from history_status hs3
                                     where hs3.code in (:FilterCanceled, :FilterFinished) -- в статусе завершено
                                     group by hs3.lawsuit_id
                                    ) s1 on (s1.lawsuit_id = hs.lawsuit_id and
                s1.start_date = hs.start_date_time)
                     join lawsuit l on l.id = hs.lawsuit_id
            order by start_date_time
                             """, namedParameters, new DataClassRowMapper<>(PreparedLawsuitRegistrRec.class));
    }
}
