CREATE DATABASE redis_study
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

SET SESSION cte_max_recursion_depth = 100000;

INSERT INTO boards (title, content, created_at)
SELECT
    CONCAT('Title', LPAD(n, 7, '0')) AS title,
    CONCAT('Content', LPAD(n, 7, '0')) AS content,
    TIMESTAMP(DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 3650) DAY) + INTERVAL FLOOR(RAND() * 86400) SECOND) AS created_at
FROM (
    SELECT n
    FROM (
        WITH RECURSIVE cte AS (
            SELECT 1 AS n
            UNION ALL
            SELECT n + 1 FROM cte WHERE n < 10000  -- 테스트용, 한 번에 10,000
        )
        SELECT n FROM cte
    ) AS numbers
) AS tmp;
