package org.example;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.terminal.Attributes;
import org.jline.utils.InfoCmp;
import org.jline.utils.NonBlockingReader;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.sql.*;
import java.util.Objects;

import com.github.freva.asciitable.AsciiTable;
import com.github.freva.asciitable.Column;


class DBUtils {
    // 数据库配置
    private static final String URL = "jdbc:mysql://localhost:3306/test_database";
    private static final String USER = "admin";
    private static final String PASSWORD = "1111";

    // 获取连接
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // 释放资源
    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("\033[31m数据库关闭出错: " + e.getMessage() + "");
        }
    }
}

class DataManager {

    // 查询整个student
    public static List<Student> queryAllStudents() throws SQLException {
        Connection conn = DBUtils.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = null;
        List<Student> students = new java.util.ArrayList<>();
        try {
            rs = stmt.executeQuery("SELECT * FROM students");
            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("gender"),
                        rs.getInt("age"),
                        rs.getString("student_no"),
                        rs.getString("create_time")
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DBUtils.close(conn, stmt, rs);
        }
        return students;
    }

    // 查询整个成绩
    public static List<Score> queryAllScores() throws SQLException {
        Connection conn = DBUtils.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = null;
        List<Score> scores = new java.util.ArrayList<>();
        try {
            rs = stmt.executeQuery("SELECT * FROM score");
            while (rs.next()) {
                scores.add(new Score(
                        rs.getInt("id"),
                        rs.getInt("student_id"),
                        rs.getString("subject"),
                        rs.getDouble("score"),
                        rs.getString("exam_time")
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DBUtils.close(conn, stmt, rs);
        }
        return scores;
    }


    // 新增单条学生数据
    public static void addStudent(Student student) throws SQLException {
        Connection conn = DBUtils.getConnection();
        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO students(name, gender, age, student_no) VALUES(?, ?, ?, ?)");
        try {
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getGender());
            pstmt.setInt(3, student.getAge());
            pstmt.setString(4, student.getStudentNo());
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DBUtils.close(conn, pstmt, null);
        }
    }


    // 根据id修改学生的姓名和年龄
    public static int updateStudentNameAndAge(int id, String newName, int newAge) throws SQLException {
        Connection conn = DBUtils.getConnection();
        PreparedStatement pstmt = conn.prepareStatement("UPDATE students SET name = ?, age = ? WHERE id = ?");
        try {
            pstmt.setString(1, newName);
            pstmt.setInt(2, newAge);
            pstmt.setInt(3, id);
            return pstmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DBUtils.close(conn, pstmt, null);
        }
    }

    // 根据id查询单个学生数据
    public static Student queryStudentById(int id) throws SQLException {
        Connection conn = DBUtils.getConnection();
        PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM students WHERE id = ?");
        ResultSet rs = null;
        try {
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("gender"),
                        rs.getInt("age"),
                        rs.getString("student_no"),
                        rs.getString("create_time")
                );
            } else
                return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DBUtils.close(conn, pstmt, rs);
        }
    }

    // 根据姓名模糊查询学生数据
    public static List<Student> queryStudentsByName(String nameKeyword) throws SQLException {
        Connection conn = DBUtils.getConnection();
        PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM students WHERE name LIKE ?");
        ResultSet rs = null;
        List<Student> students = new java.util.ArrayList<>();
        try {
            pstmt.setString(1, "%" + nameKeyword + "%");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("gender"),
                        rs.getInt("age"),
                        rs.getString("student_no"),
                        rs.getString("create_time")
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DBUtils.close(conn, pstmt, rs);
        }
        return students;
    }

    // 新增单条学生成绩
    public static void addScore(Score score) throws SQLException {
        Connection conn = DBUtils.getConnection();
        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO score(student_id, subject, score, exam_time) VALUES(?, ?, ?, ?)");
        try {
            pstmt.setInt(1, score.getStudentId());
            pstmt.setString(2, score.getSubject());
            pstmt.setDouble(3, score.getScore());
            pstmt.setString(4, score.getExamTime());
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DBUtils.close(conn, pstmt, null);
        }
    }

    // 根据student_id删除学生的所有成绩
    public static int deleteScoresByStudentId(int studentId) throws SQLException {
        Connection conn = DBUtils.getConnection();
        PreparedStatement pstmt = conn.prepareStatement("DELETE FROM score WHERE student_id = ?");
        try {
            pstmt.setInt(1, studentId);
            return pstmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DBUtils.close(conn, pstmt, null);
        }
    }

    // 根据id删除学生数据及其所有成绩 (带事务控制)
    public static int deleteStudentById(int id) throws SQLException {
        Connection conn = DBUtils.getConnection();
        PreparedStatement pstmtScore = conn.prepareStatement("DELETE FROM score WHERE student_id = ?");
        PreparedStatement pstmtStudent = conn.prepareStatement("DELETE FROM students WHERE id = ?");
        int result = 0;
        try {
            conn.setAutoCommit(false); // 开启事务

            pstmtScore.setInt(1, id);
            pstmtScore.executeUpdate();

            pstmtStudent.setInt(1, id);
            result = pstmtStudent.executeUpdate();

            conn.commit(); // 提交事务
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                UI.printError("回滚失败: " + ex.getMessage());
            }
            throw new RuntimeException("删除失败已回滚: " + e.getMessage(), e);
        } finally {
            DBUtils.close(null, pstmtScore, null);
            DBUtils.close(conn, pstmtStudent, null);
        }
        return result;
    }

    // 根据学号查询指定学生的所有科目成绩
    public static List<Score> queryScoresByStudentNo(String studentNo) throws SQLException {
        Connection conn = DBUtils.getConnection();
        PreparedStatement pstmt = conn.prepareStatement("SELECT sc.* FROM score sc INNER JOIN students st ON sc.student_id = st.id WHERE st.student_no = ?");
        ResultSet rs = null;
        List<Score> scores = new java.util.ArrayList<>();
        try {
            pstmt.setString(1, studentNo);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                scores.add(new Score(
                        rs.getInt("id"),
                        rs.getInt("student_id"),
                        rs.getString("subject"),
                        rs.getDouble("score"),
                        rs.getString("exam_time")
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DBUtils.close(conn, pstmt, rs);
        }
        return scores;
    }

    // 批量新增学生
    public static int[] addStudentsBatch(List<Student> students) throws SQLException {
        Connection conn = DBUtils.getConnection();
        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO students(name, gender, age, student_no) VALUES(?, ?, ?, ?)");
        int[] results = null;

        try {

            for (Student student : students) {
                pstmt.setString(1, student.getName());
                pstmt.setString(2, student.getGender());
                pstmt.setInt(3, student.getAge());
                pstmt.setString(4, student.getStudentNo());

                pstmt.addBatch();
            }

            results = pstmt.executeBatch();

        } catch (Exception e) {

            throw new RuntimeException("批量插入失败， " + e.getMessage(), e);
        } finally {
            DBUtils.close(conn, pstmt, null);
        }

        return results; // 每条sql影响的行数
    }
}

class UI {

    private static Terminal terminal;

    static {
        try {
            terminal = TerminalBuilder.builder().system(true).build();
        } catch (IOException e) {
            System.err.println("\033[31m菜单显示出错: " + e.getMessage() + "\033[0m");
        }
    }

    // 默认清屏
    public static int showMenu(String title, List<String> options) {
        return showMenu(title, options, true);
    }

    // 可选不清屏
    public static int showMenu(String title, List<String> options, boolean clearScreen) {
        if (terminal == null)
            return -1;
        int selectedIndex = 0;

        Attributes originalAttributes = terminal.enterRawMode();
        try {
            NonBlockingReader reader = terminal.reader();
            terminal.puts(InfoCmp.Capability.cursor_invisible);

            // 对于不需要clear先打印一个空行分隔
            if (!clearScreen) {
                terminal.writer().println();
            }

            boolean firstRun = true;

            while (true) {
                if (clearScreen) {
                    terminal.puts(InfoCmp.Capability.clear_screen); // 清理
                    terminal.puts(InfoCmp.Capability.cursor_home); // 光标归位
                }
                if (!firstRun) {
                    // 移动光标回到开始位置,以免覆盖
                    terminal.writer().print("\033[" + (options.size() + 3) + "A");
                }

                firstRun = false;

                var writer = terminal.writer();
                writer.println("\033[44m=== " + title + " ===\033[0m");

                for (int i = 0; i < options.size(); i++) {
                    if (i == selectedIndex) {
                        // 选中项：绿色高亮，前面加 >
                        writer.println("\033[32m> " + options.get(i) + "\033[0m");
                    } else {
                        // 未选中项：普通显示
                        writer.println("  " + options.get(i));
                    }
                }
                writer.println("使用 'w/s' 上下移动，'Enter' 确认\n");

                terminal.flush();

                // 读按键
                int input = reader.read();

                if (input == 'w' || input == 'W') {
                    if (selectedIndex > 0)
                        selectedIndex--;
                } else if (input == 's' || input == 'S') {
                    if (selectedIndex < options.size() - 1)
                        selectedIndex++;
                } else if (input == 13 || input == 10) {
                    // Enter
                    return selectedIndex;
                } else if (input == 'q') {
                    // 按 q 退出
                    return -1;
                }
            }
        } catch (IOException e) {
            printError("菜单显示出错: " + e.getMessage());
            return -1;
        } finally {
            terminal.setAttributes(originalAttributes);
            terminal.puts(InfoCmp.Capability.cursor_visible);
            terminal.flush();
        }
    }

    public static String readInput(String prompt) {
        if (terminal == null)
            return "";
        LineReader lineReader = LineReaderBuilder.builder().terminal(terminal).build();
        return lineReader.readLine(prompt);
    }

    public static void println(String message) {
        if (terminal == null)
            return;
        terminal.writer().println(message);
        terminal.flush();
    }

    public static void pause() {
        if (terminal == null)
            return;
        Attributes originalAttributes = terminal.enterRawMode();
        try {
            NonBlockingReader reader = terminal.reader();
            terminal.writer().println("\n按任意键继续...");
            terminal.flush();
            reader.read();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            terminal.setAttributes(originalAttributes);
        }
    }

    public static void printError(String message) {
        println("\033[31m" + message + "\033[0m");
        pause();
    }

}

public class Main {


    static void main(String[] args) {

        while (true) {

            showAllData();


            int selected = UI.showMenu("学生管理系统", Arrays.asList(
                    "新增学生",
                    "新增成绩",
                    "修改学生信息",
                    "删除学生及成绩",
                    "删除某学生所有成绩",
                    "查询学生-根据ID",
                    "查询学生-根据姓名",
                    "查询成绩-根据学号",
                    "批量新增学生",
                    "退出(按下q)"
            ), false);

            switch (selected) {
                case 0:
                    addStudent();
                    break;
                case 1:
                    addScore();
                    break;
                case 2:
                    updateStudent();
                    break;
                case 3:
                    deleteStudents();
                    break;
                case 4:
                    deleteScoresOnly();
                    break;
                case 5:
                    queryStudentById();
                    break;
                case 6:
                    queryStudentByName();
                    break;
                case 7:
                    queryScoresByNo();
                    break;
                case 8:
                    addStudentsBatch();
                    break;
                case 9:
                case -1:
                    UI.printError("退出");
                    System.exit(0);
                    break;
                default:
                    break;
            }
        }

    }

    private static void showAllData() {
        UI.println("所有学生:");
        try {
            List<Student> students = DataManager.queryAllStudents();
            if (students.isEmpty()) {
                UI.println("没有学生数据");
            } else {
                String table = AsciiTable.getTable(students,
                        Arrays.asList(new Column().header("ID").with(s -> String.valueOf(s.getId())),
                                new Column().header("姓名").with(Student::getName),
                                new Column().header("性别").with(Student::getGender),
                                new Column().header("年龄").with(s -> String.valueOf(s.getAge())),
                                new Column().header("学号").with(Student::getStudentNo),
                                new Column().header("创建时间").with(Student::getCreateTime))
                );
                UI.println(table);
            }
        } catch (SQLException e) {
            UI.printError("查询学生数据失败: " + e.getMessage());
        }

        UI.println("所有成绩:");
        try {
            List<Score> scores = DataManager.queryAllScores();
            if (scores.isEmpty()) {
                UI.println("没有成绩数据");
            } else {
                String table = AsciiTable.getTable(scores,
                        Arrays.asList(new Column().header("ID").with(s -> String.valueOf(s.getId())),
                                new Column().header("学生ID").with(s -> String.valueOf(s.getStudentId())),
                                new Column().header("科目").with(Score::getSubject),
                                new Column().header("分数").with(s -> String.valueOf(s.getScore())),
                                new Column().header("考试日期").with(Score::getExamTime))
                );
                UI.println(table);
            }
        } catch (SQLException e) {
            UI.printError("查询成绩数据失败: " + e.getMessage());
        }
    }

    private static void addStudent() {
        try {
            String name = UI.readInput("请输入学生姓名: ");
            String gender = UI.readInput("请输入学生性别 (男/女): ");
            int age = Integer.parseInt(UI.readInput("请输入学生年龄: "));
            String studentNo = UI.readInput("请输入学生学号: ");
            DataManager.addStudent(new Student(0, name, gender, age, studentNo, null));
            UI.println("学生添加成功!");
        } catch (Exception e) {
            UI.printError("添加学生失败: " + e.getMessage());
        }
    }

    private static void addScore() {
        try {
            int studentId = Integer.parseInt(UI.readInput("请输入关联学生ID: "));
            String subject = UI.readInput("请输入科目名称: ");
            double scoreVal = Double.parseDouble(UI.readInput("请输入成绩(满分100): "));
            String examTime = UI.readInput("请输入考试日期 (格式 YYYY-MM-DD): ");
            DataManager.addScore(new Score(0, studentId, subject, scoreVal, examTime));
            UI.println("成绩添加成功!");
        } catch (Exception e) {
            UI.printError("添加成绩失败: " + e.getMessage());
        }
    }

    private static void updateStudent() {
        try {
            int id = Integer.parseInt(UI.readInput("请输入要修改的学生ID: "));
            String newName = UI.readInput("请输入新的姓名: ");
            int newAge = Integer.parseInt(UI.readInput("请输入新的年龄: "));
            int affected = DataManager.updateStudentNameAndAge(id, newName, newAge);
            if (affected > 0) {
                UI.println("学生信息修改成功!");
            } else {
                throw new RuntimeException("未找到该ID的学生");
            }
        } catch (Exception e) {
            UI.printError("修改失败: " + e.getMessage());
        }
    }

    private static void deleteStudents() {
        try {
            int id = Integer.parseInt(UI.readInput("请输入要删除的学生ID: "));
            int affected = DataManager.deleteStudentById(id);
            if (affected > 0) {
                UI.println("学生及其成绩删除成功!");
            } else {
                throw new RuntimeException("未找到该ID的学生");
            }
        } catch (Exception e) {
            UI.printError("删除失败: " + e.getMessage());
        }
    }

    private static void deleteScoresOnly() {
        try {
            int studentId = Integer.parseInt(UI.readInput("请输入学生ID以删除其所有成绩: "));
            int affected = DataManager.deleteScoresByStudentId(studentId);
            UI.println("成功删除了 " + affected + " 条成绩记录");
        } catch (Exception e) {
            UI.printError("成绩删除失败: " + e.getMessage());
        }
    }

    private static void queryStudentById() {
        try {
            int id = Integer.parseInt(UI.readInput("请输入要查询的学生ID: "));
            Student student = DataManager.queryStudentById(id);
            if (student != null) {
                UI.println("查询结果:");
                UI.println(student.toString());
            } else {
                UI.println("\033[33m查无此人!");
            }
        } catch (Exception e) {
            UI.printError("查询出错: " + e.getMessage());
        }
        UI.pause();
    }

    private static void queryStudentByName() {
        try {
            String nameKeyword = UI.readInput("请输入要模糊查询的姓名: ");
            List<Student> students = DataManager.queryStudentsByName(nameKeyword);
            if (students.isEmpty()) {
                throw new RuntimeException("没有匹配的学生");
            } else {
                String table = AsciiTable.getTable(students,
                        Arrays.asList(new Column().header("ID").with(s -> String.valueOf(s.getId())),
                                new Column().header("姓名").with(Student::getName),
                                new Column().header("性别").with(Student::getGender),
                                new Column().header("年龄").with(s -> String.valueOf(s.getAge())),
                                new Column().header("学号").with(Student::getStudentNo),
                                new Column().header("创建时间").with(Student::getCreateTime))
                );
                UI.println(table);
            }
        } catch (Exception e) {
            UI.printError("查询出错: " + e.getMessage());
        }
        UI.pause();
    }

    private static void queryScoresByNo() {
        try {
            String studentNo = UI.readInput("请输入要查询成绩的学生学号: ");
            List<Score> scores = DataManager.queryScoresByStudentNo(studentNo);
            if (scores.isEmpty()) {
                throw new RuntimeException("没有找到该学号的学生或该学生没有成绩");
            } else {
                String table = AsciiTable.getTable(scores,
                        Arrays.asList(new Column().header("成绩ID").with(s -> String.valueOf(s.getId())),
                                new Column().header("学生ID").with(s -> String.valueOf(s.getStudentId())),
                                new Column().header("科目").with(Score::getSubject),
                                new Column().header("分数").with(s -> String.valueOf(s.getScore())),
                                new Column().header("考试日期").with(Score::getExamTime))
                );
                UI.println(table);
            }
        } catch (Exception e) {
            UI.printError("查询成绩出错: " + e.getMessage());
        }
        UI.pause();
    }


    private static void addStudentsBatch() {
        try {
            List<Student> list = new java.util.ArrayList<>();
            UI.println("批量录入(输入姓名 'q' 结束录入)");

            while (true) {
                String name = UI.readInput("请输入学生姓名: ");
                if (Objects.equals(name, "q")) break;
                String gender = UI.readInput("请输入学生性别 (男/女): ");
                int age = Integer.parseInt(UI.readInput("请输入学生年龄: "));
                String studentNo = UI.readInput("请输入学生学号: ");

                list.add(new Student(0, name, gender, age, studentNo, null));
                UI.println("已加入队列，当前" + list.size() + "条待插入\n");
            }

            int[] results = DataManager.addStudentsBatch(list);
            UI.println("成功批量插入 " + results.length + " 条学生数据!");

        } catch (Exception e) {
            UI.printError("批量添加失败: " + e.getMessage());
        }
        UI.pause();
    }

}
