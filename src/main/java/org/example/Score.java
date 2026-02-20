package org.example;

/*
CREATE TABLE score
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT           NOT NULL,
    subject    VARCHAR(50)   NOT NULL,
    score      DECIMAL(5, 2) NOT NULL CHECK (score >= 0 AND score <= 100),
    exam_time  DATE          NOT NULL,

    FOREIGN KEY (student_id) REFERENCES students (id) ON DELETE CASCADE
);
 */
public class Score {
    private int id;
    private int studentId;
    private String subject;
    private double score;
    private String examTime;

    public Score(int id, int studentId, String subject, double score, String examTime) {
        this.id = id;
        this.studentId = studentId;
        this.subject = subject;
        this.score = score;
        this.examTime = examTime;
    }

    public int getId() {
        return id;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getSubject() {
        return subject;
    }

    public double getScore() {
        return score;
    }

    public String getExamTime() {
        return examTime;
    }

    @Override
    public String toString() {
        return "成绩\n" +
                " id:" + id +
                " studentId:" + studentId +
                " subject:" + subject +
                " score:" + score +
                " examTime:" + examTime;
    }

}
