package com.example.demo.repository;

import com.example.demo.dto.response.PageResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
public class SearchRepository {

    @PersistenceContext
    private EntityManager entityManager;
    PageResponse<?> getAllUserWithSortByColumsAndSearch(int pageNo, int pageSize, String search, String sortBy){

        StringBuilder sqlQuery = new StringBuilder("select u from User u where 1=1");
        if (StringUtils.hasLength(search)) {
            sqlQuery.append("and u.firstName like lower(:firstName)");
            sqlQuery.append("or u.lastName like lower(:lastName)");
            sqlQuery.append("or u.email like lower(:email)");
        }
        Query selectQuery = entityManager.createQuery(sqlQuery.toString());
        selectQuery.setFirstResult(pageNo);
        selectQuery.setMaxResults(pageSize);
        if (StringUtils.hasLength(search)) {
            selectQuery.setParameter("firstName","%" + search + "%");
            selectQuery.setParameter("lastName","%" + search + "%");
            selectQuery.setParameter("email", String.format("%%%s%%", search));
        }
        List user =selectQuery.getResultList();
        // query list user

        // query so record
        return null;
    }
}
