SHOW DATABASES;

CREATE
    DATABASE test_database;

USE
    test_database;

CREATE TABLE students (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50)       NOT NULL,
    gender      ENUM ('男', '女') NOT NULL,
    age         TINYINT           NOT NULL,
    student_no  VARCHAR(10)       NOT NULL UNIQUE,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                      );

CREATE TABLE score (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT           NOT NULL,
    subject    VARCHAR(50)   NOT NULL,
    score      DECIMAL(5, 2) NOT NULL CHECK (score >= 0 AND score <= 100),
    exam_time  DATE          NOT NULL,

    FOREIGN KEY (student_id) REFERENCES students (id) ON DELETE CASCADE
                   );

# 插入测试数据
INSERT INTO students (name, gender, age, student_no)
VALUES ('122', '女', 20, '312331'),
         ('231', '男', 22, '42223'),
         ('434', '男', 21, '11211');

INSERT INTO score (student_id, subject, score, exam_time)
VALUES (1, '语文', 85.5, '2026-01-15'),
         (1, '数学', 92.0, '2027-01-15'),
         (2, '语文', 78.0, '2028-01-15'),
         (2, '数学', 88.5, '2029-01-15'),
         (3, '语文', 90.0, '2026-01-15'),
         (3, '数学', 95.0, '2027-01-15');