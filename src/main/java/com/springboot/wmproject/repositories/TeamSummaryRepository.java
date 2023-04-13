package com.springboot.wmproject.repositories;

import com.springboot.wmproject.entities.TeamSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamSummaryRepository extends JpaRepository<TeamSummary,Integer> {
    String getSummaryTeamOrganization = """
            SELECT
            Q3.team_id,
            Q3.team_name,
            CASE WHEN Q3.total_members IS NULL THEN FALSE ELSE Q3.total_members END as total_members,
            Q3.leader_name,
            Q3.emp_id,
            CASE WHEN Q4.has_confirm_order IS NULL THEN FALSE ELSE Q4.has_confirm_order END as has_confirm_order
            FROM
            (SELECT
            Q1.team_id,
            Q1.team_name AS team_name,
            Q1.Total AS total_members,
            Q2.name AS leader_name,
            Q2.id AS emp_id
            FROM
            (SELECT
            o.id AS team_id,
            o.team_name AS team_name,
            COUNT(e.id) - e.is_deleted AS Total
            FROM
            employees e
            RIGHT JOIN organize_teams o ON e.team_id = o.id
            WHERE
            o.is_deleted = 0
            GROUP BY o.id , o.team_name , e.is_deleted
            LIMIT 0 , 1000) AS Q1
            LEFT JOIN (SELECT
            e.team_id, o.team_name, MAX(e.name) AS name, MAX(e.id) AS id
            FROM
            employees e
            LEFT JOIN organize_teams o ON e.team_id = o.id
            WHERE
            e.is_leader = 1 AND o.is_deleted = 0
            AND e.is_deleted = 0
            GROUP BY e.team_id , o.team_name
            LIMIT 0 , 1000) AS Q2 ON Q1.team_id = Q2.team_id) AS Q3
            LEFT JOIN
            (SELECT Q1.team_id, Q1.team_name, Q1.has_confirm_order,
            CASE WHEN Q2.Total IS NULL THEN FALSE ELSE Q2.Total END as Total
            FROM (
            SELECT
            o.id AS team_id,
            o.team_name,
            CASE WHEN od.order_status = 'confirm' THEN TRUE ELSE FALSE END AS has_confirm_order
            FROM organize_teams o
            JOIN orders od ON o.id = od.organize_team
            WHERE od.order_status = 'confirm'
            GROUP BY o.id, o.team_name, has_confirm_order
            ) AS Q1
            JOIN (
            SELECT
            o.id AS team_id,
            o.team_name AS team_name,
            COUNT(e.id) - e.is_deleted AS Total
            FROM employees e
            RIGHT JOIN organize_teams o ON e.team_id = o.id
            WHERE o.is_deleted = 0
            GROUP BY o.id, o.team_name, e.is_deleted
            LIMIT 0, 1000
            ) AS Q2 ON Q1.team_id = Q2.team_id
            GROUP BY Q1.team_id, Q1.team_name, Q1.has_confirm_order, Q2.total) AS Q4 ON Q3.team_id = Q4.team_id
            """;

    @Query(value = getSummaryTeamOrganization, nativeQuery = true)
    List<TeamSummary> getSummaryTeamOrganization();
}
