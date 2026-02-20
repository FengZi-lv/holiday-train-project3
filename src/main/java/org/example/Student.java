package org.example;

/*
 CREATE TABLE students
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50) NOT NULL,
    gender      ENUM ('男', '女') NOT NULL,
    age         TINYINT     NOT NULL,
    student_no  VARCHAR(10) NOT NULL UNIQUE,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
 */

public class Student {
    private int id;
    private String name;
    private String gender;
    private int age;
    private String studentNo;
    private String createTime;

    public Student(int id, String name, String gender, int age, String studentNo, String createTime) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.studentNo = studentNo;
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public String getCreateTime() {
        return createTime;
    }

    @Override
    public String toString() {
        return "学生\n" +
                " id:" + id +
                " name:" + name +
                " gender:" + gender +
                " age:" + age +
                " studentNo:" + studentNo +
                " createTime:" + createTime;
    }

}