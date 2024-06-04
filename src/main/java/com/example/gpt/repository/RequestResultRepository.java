package com.example.gpt.repository;

import com.example.gpt.data.RequestResult;
import com.example.gpt.data.RequestResult.Fields;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
@RequiredArgsConstructor
public class RequestResultRepository {
  private final MongoTemplate mongoTemplate;

  public RequestResult save(RequestResult result) {
    return mongoTemplate.save(result);
  }

  public RequestResult getById(String id) {
    Query query = new Query();
    query.addCriteria(where(Fields.id).is(id));
    return mongoTemplate.findOne(query, RequestResult.class);
  }

  public void updateResult(String id, String result, boolean completed) {
    mongoTemplate.updateFirst(
        query(where(Fields.id).is(id)),
        Update.update(Fields.result, result).set(Fields.completed, completed),
        RequestResult.class
    );
  }
}
