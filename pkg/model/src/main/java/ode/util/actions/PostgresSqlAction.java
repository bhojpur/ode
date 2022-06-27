package ode.util.actions;

// Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

import com.google.common.collect.Iterables;
import ode.conditions.InternalException;
import ode.util.SqlAction;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PostgresSqlAction extends SqlAction.Impl {

    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate namedJdbc;

    public PostgresSqlAction(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.namedJdbc = new NamedParameterJdbcTemplate(jdbc);
    }

    //
    // Impl methods
    //


    @Override
    protected JdbcTemplate _jdbc() {
        return this.jdbc;
    }

    @Override
    protected NamedParameterJdbcTemplate _namedJdbc() {
        return namedJdbc;
    }

    @Override
    protected String _lookup(String key) {
        return PsqlStrings.getString("sql_action." + key);
    }

    //
    // Interface methods
    //

    /**
     * The temp_ids infrastructure was never properly put
     * in place for the "psql" profile. This method simply
     * bypasses all query rewriting until that's functional.
     */
    public String rewriteHql(String query, String key, Object value) {
        return query;
    }

    public void prepareSession(final long eventId, final long userId, final long groupId) {
        SimpleJdbcCall call = new SimpleJdbcCall(_jdbc()).withFunctionName("_prepare_session"); // FIXME
        MapSqlParameterSource in = new MapSqlParameterSource();
        in.addValue("_event_id", eventId);
        in.addValue("_user_id", userId);
        in.addValue("_group_id", groupId);
        call.executeFunction(void.class, in);
    }

    public boolean activeSession(String sessionUUID) {
        int count = _jdbc().queryForObject(_lookup("active_session"), Integer.class, //$NON-NLS-1$
                sessionUUID);
        return count > 0;
    }

    private final static String synchronizeJobsSql = PsqlStrings
            .getString("sql_action.sync_jobs"); //$NON-NLS-1$

    public int synchronizeJobs(List<Long> ids) {
        int count = 0;
        if (ids.size() > 0) {
            Map<String, Object> m = new HashMap<>();
            m.put("ids", ids); //$NON-NLS-1$
            count += _namedJdbc().update(
                    synchronizeJobsSql + _lookup("id_not_in"), m); //$NON-NLS-1$
        } else {
            count += _jdbc().update(synchronizeJobsSql);
        }
        return count;
    }

    public List<Long> findRepoPixels(String uuid, String dirname, String basename) {
        return _jdbc().query(_lookup("find_repo_pixels"), //$NON-NLS-1$
                (arg0, arg1) -> arg0.getLong(1), uuid, dirname, basename);
    }

    public Long findRepoImageFromPixels(long id) {
        return _jdbc().queryForObject(_lookup("find_repo_image_from_pixels"), Long.class, id); //$NON-NLS-1$
    }

    public Long nextSessionId() {
        return _jdbc().queryForObject(_lookup("next_session"), Long.class); //$NON-NLS-1$
    }

    public Map<String, Object> repoFile(long value) {
        return _jdbc().queryForMap(_lookup("repo_file"), value); //$NON-NLS-1$
    }

    public long countFormat(String name) {
        return _jdbc().queryForObject(_lookup("count_format"), Long.class, name); //$NON-NLS-1$
    }

    // Copied from data.vm
    @Deprecated  // use ode.services.util.EnsureEnum
    public final static String insertFormatSql = PsqlStrings
            .getString("sql_action.insert_format"); //$NON-NLS-1$

    @Deprecated  // use ode.services.util.EnsureEnum
    public int insertFormat(String name) {
        return _jdbc().update(insertFormatSql, name);
    }

    public int closeSessions(String uuid) {
        return _jdbc().update(_lookup("update_session"), uuid); //$NON-NLS-1$
    }

    public long nodeId(String internal_uuid) {
        return _jdbc().queryForObject(_lookup("internal_uuid"), Long.class,//$NON-NLS-1$
                internal_uuid);
    }

    public int insertSession(Map<String, Object> params) {
        return _namedJdbc().update(_lookup("insert_session"), params); //$NON-NLS-1$
    }

    public int updateSessionUserIP(long sessionId, String userIP) {
        return _jdbc().update(_lookup("update_session_user_ip"), userIP, sessionId); //$NON-NLS-1$
    }

    public Long sessionId(String uuid) {
        return _jdbc().queryForObject(_lookup("session_id"), Long.class, uuid); //$NON-NLS-1$
    }

    public int removePassword(Long id) {
        return _jdbc().update(_lookup("remove_pass"), id); //$NON-NLS-1$
    }

    public Date now() {
        return _jdbc().queryForObject(_lookup("now"), Date.class); //$NON-NLS-1$
    }

    public int updateConfiguration(String key, String value) {
        return _jdbc().update(_lookup("update_config"), value, //$NON-NLS-1$
                key);
    }

    public String dbVersion() {
        return _jdbc().query(_lookup("db_version"), //$NON-NLS-1$
                (arg0, arg1) -> {
                    String v = arg0.getString("currentversion"); //$NON-NLS-1$
                    int p = arg0.getInt("currentpatch"); //$NON-NLS-1$
                    return v + "__" + p; //$NON-NLS-1$
                }).get(0);
    }

    public String dbUuid() {
        return _jdbc().query(_lookup("db_uuid"), //$NON-NLS-1$
                (arg0, arg1) -> {
                    String s = arg0.getString("value"); //$NON-NLS-1$
                    return s;
                }).get(0);
    }

    private final static String logLoaderQuerySql = PsqlStrings
            .getString("sql_action.log_loader_query"); //$NON-NLS-1$
    private final static String logLoaderInsertSql = PsqlStrings
            .getString("sql_action.log_loader_insert"); //$NON-NLS-1$
    private final static String logLoaderUpdateSql = PsqlStrings
            .getString("sql_action.log_loader_update"); //$NON-NLS-1$
    private final static String logLoaderDeleteSql = PsqlStrings
            .getString("sql_action.log_loader_delete"); //$NON-NLS-1$

    public long selectCurrentEventLog(String key) {
        return _jdbc().queryForObject(logLoaderQuerySql, Long.class, key);
    }

    public void setCurrentEventLog(long id, String key) {
        int count = _jdbc().update(logLoaderUpdateSql, id, key);
        if (count == 0) {
            _jdbc().update(logLoaderInsertSql, key, id);
        }
    }

    public void delCurrentEventLog(String key) {
        _jdbc().update(logLoaderDeleteSql, key);
    }

    public long nextValue(String segmentValue, int incrementSize) {
        return _jdbc().queryForObject(_lookup("next_val"), Long.class, //$NON-NLS-1$
                segmentValue, incrementSize);
    }

    public long currValue(String segmentName) {
        try {
            return _jdbc().queryForObject(_lookup("curr_val"), Long.class, //$NON-NLS-1$
                    segmentName);
        } catch (EmptyResultDataAccessException erdae) {
            return -1L;
        }
    }

    public void insertLogs(List<Object[]> batchData) {
        _jdbc().batchUpdate(_lookup("insert_logs"), batchData); //$NON-NLS-1$
    }

    public List<Map<String, Object>> roiByImage(final long imageId) {
        String queryString = _lookup("roi_by_image"); //$NON-NLS-1$
        return _jdbc().queryForList(queryString,
                imageId);
    }

    public List<Long> getShapeIds(long roiId) {
        return _jdbc().query(_lookup("shape_ids"), //$NON-NLS-1$
                new IdRowMapper(), roiId);
    }

    @Override
    public void setFileRepo(Collection<Long> ids, String repoId) {
        for (final List<Long> idsBatch : Iterables.partition(ids, 256)) {
            final Map<String, Object> params = new HashMap<>();
            params.put("ids", idsBatch);
            params.put("repo", repoId);

            _namedJdbc().update(_lookup("set_file_repo"), //$NON-NLS-1$
                    params);
        }
    }

    public void setPixelsNamePathRepo(long pixId, String name, String path,
                                      String repoId) {
        _jdbc().update(_lookup("update_pixels_name"), name, pixId); //$NON-NLS-1$
        _jdbc().update(_lookup("update_pixels_path"), path, pixId); //$NON-NLS-1$
        _jdbc().update(_lookup("update_pixels_repo"), repoId, //$NON-NLS-1$
                pixId);
    }

    public List<Long> getDeletedIds(String entityType) {
        String sql = _lookup("get_delete_ids"); //$NON-NLS-1$
        RowMapper<Long> mapper = (resultSet, rowNum) -> new Long(resultSet.getString(1));
        return _jdbc().query(sql, mapper, entityType);
    }

    public void createSavepoint(String savepoint) {
        call("SAVEPOINT DEL", savepoint);
    }

    public void releaseSavepoint(String savepoint) {
        call("RELEASE SAVEPOINT DEL", savepoint);
    }

    public void rollbackSavepoint(String savepoint) {
        call("ROLLBACK TO SAVEPOINT DEL", savepoint);
    }

    private void call(final String call, final String savepoint) {
        _jdbc().execute((ConnectionCallback<Object>) con -> {
            con.prepareCall(call + savepoint).execute(); // TODO Use a difference callback
            return null;
        });
    }

    public void deferConstraints() {
        _jdbc().execute((ConnectionCallback<Object>) con -> {
            Statement statement = con.createStatement();
            statement.execute("set constraints all deferred;");
            return null;
        });
    }

    public Set<String> currentUserNames() {
        List<String> names = _jdbc().query(_lookup("current_user_names"),  //$NON-NLS-1$
                (arg0, arg1) -> {
                    return arg0.getString(1); // Bleck
                });
        return new HashSet<>(names);
    }

    /* (non-Javadoc)
     * @see ode.util.SqlAction#getPixelsNamePathRepo(long)
     */
    public List<String> getPixelsNamePathRepo(long id)
            throws InternalException {
        try {
            return _jdbc().queryForObject(
                    _lookup("get_pixels_name_path_repo"), //$NON-NLS-1$
                    (arg0, arg1) -> {
                        final List<String> values = new ArrayList<>();
                        values.add(arg0.getString(1));
                        values.add(arg0.getString(2));
                        values.add(arg0.getString(3));
                        return values;
                    }, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (UncategorizedSQLException e) {
            handlePotentialPgArrayJarError(e);
            return null;
        }
    }

    //
    // End PgArrayHelper
    //

    //
    // Helpers
    //

    /**
     * If postgresql is installed with an older jdbc jar that is on the
     * bootstrap classpath, then it's possible that the use of pgarrays will
     * fail (I think). See #7432
     */
    protected void handlePotentialPgArrayJarError(UncategorizedSQLException e) {
        log.error(e.toString()); // slf4j migration: toString()
        throw new InternalException(
                "Potential jdbc jar error during pgarray access (See #7432)\n"
                        + printThrowable(e));
    }
}