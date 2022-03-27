package com.bzavoevanyy.dao;

import com.yandex.ydb.table.TableClient;
import com.yandex.ydb.table.query.DataQueryResult;
import com.yandex.ydb.table.query.Params;
import com.yandex.ydb.table.transaction.TxControl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@RequiredArgsConstructor
@Component
public class EntityManager {

    private final TableClient tableClient;

    public void execute(String query, Params params, Consumer<DataQueryResult> callback) {

        var session = tableClient.createSession()
                .join()
                .expect("Error: can't create session");

        var preparedQuery = session.prepareDataQuery(query)
                .join()
                .expect("Error: query preparation failed");

        var result = preparedQuery.execute(TxControl.serializableRw().setCommitTx(true), params)
                .join()
                .expect("Error: query execution failed");

        if (callback != null) {
            callback.accept(result);
        }

    }

    public void execute(String query, Params params) {
        execute(query, params, null);
    }
}
