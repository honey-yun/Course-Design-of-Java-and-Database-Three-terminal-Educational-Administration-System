package UI;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import server.DBUtil;

public class StudentFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private DefaultTableModel personalInfoModel;
    private DefaultTableModel courseModel;
    private DefaultTableModel scoreModel;
    private JTable courseTable;

    public StudentFrame() {
        super("学生界面");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        tabbedPane = new JTabbedPane(JTabbedPane.LEFT);

        // 个人信息选项卡
        JPanel personalInfoPanel = createPersonalInfoPanel();
        tabbedPane.add("个人信息", personalInfoPanel);

        // 选课选项卡
        JPanel courseSelectionPanel = createCourseSelectionPanel();
        tabbedPane.add("选课", courseSelectionPanel);

        // 成绩查询选项卡
        JPanel scoreQueryPanel = createScoreQueryPanel();
        tabbedPane.add("成绩查询", scoreQueryPanel);

        add(tabbedPane);
    }

    // 创建个人信息选项卡面板
    private JPanel createPersonalInfoPanel() {
        JPanel personalInfoPanel = new JPanel();
        String[] personalInfoTitles = {"学号", "姓名", "性别", "年龄", "专业"};
        personalInfoModel = new DefaultTableModel(null, personalInfoTitles);
        JTable personalInfoTable = new JTable(personalInfoModel);
        personalInfoTable.setRowHeight(20);
        JScrollPane personalInfoScrollPane = new JScrollPane(personalInfoTable);
        personalInfoScrollPane.setPreferredSize(new java.awt.Dimension(550, 100));

        try {
            // 检查用户名是否已设置，如果未设置则提示用户登录
            if (LoginFrame.getUsername() == null || LoginFrame.getUsername().isEmpty()) {
                JOptionPane.showMessageDialog(null, "请先登录", "错误", JOptionPane.ERROR_MESSAGE);
                dispose();
                return null;
            }
            showPersonalInfo(personalInfoModel);
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        personalInfoPanel.setLayout(null);
        personalInfoScrollPane.setBounds(50, 190, 550, 70);
        personalInfoPanel.add(personalInfoScrollPane);
        return personalInfoPanel;
    }

    // 创建选课选项卡面板
    private JPanel createCourseSelectionPanel() {
        JPanel courseSelectionPanel = new JPanel();
        JLabel courseSelectionLabel = new JLabel("请输入你想选的课的课程号：");
        JTextField courseNoField = new JTextField(20);
        JButton selectCourseButton = new JButton("选课");

        courseSelectionLabel.setFont(new Font("楷体", Font.BOLD, 17));
        selectCourseButton.setFont(new Font("楷体", Font.PLAIN, 17));

        // 设置按钮背景透明
        selectCourseButton.setContentAreaFilled(false);

        courseSelectionLabel.setBounds(50, 50, 250, 30);
        courseNoField.setBounds(300, 50, 150, 30);
        selectCourseButton.setBounds(500, 50, 80, 30);

        // 课程信息表格相关组件
        String[] courseTitles = {"课程号", "课程名", "学分"};
        courseModel = new DefaultTableModel(null, courseTitles);
        courseTable = new JTable(courseModel);
        courseTable.setRowHeight(20);
        JScrollPane courseScrollPane = new JScrollPane(courseTable);
        courseScrollPane.setPreferredSize(new java.awt.Dimension(550, 100));
        courseScrollPane.setBounds(50, 100, 550, 220);

        // 查询并显示所有课程信息
        try {
            showAllCourses(courseModel);
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        courseSelectionPanel.setLayout(null);
        courseSelectionPanel.add(courseSelectionLabel);
        courseSelectionPanel.add(courseNoField);
        courseSelectionPanel.add(selectCourseButton);
        courseSelectionPanel.add(courseScrollPane);

        // 选课按钮点击事件处理
        selectCourseButton.addActionListener(e -> {
            String courseNo = courseNoField.getText().trim();
            if (courseNo.isEmpty()) {
                JOptionPane.showMessageDialog(null, "请输入课程号", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                if (selectCourse(courseNo)) {
                    JOptionPane.showMessageDialog(null, "选课成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "选课失败，可能课程号不存在或已选过该课程", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });
        return courseSelectionPanel;
    }

    // 创建成绩查询选项卡面板
    private JPanel createScoreQueryPanel() {
        JPanel scoreQueryPanel = new JPanel();
        JLabel courseNoQueryLabel = new JLabel("课程号：");
        JTextField courseNoQueryField = new JTextField(20);
        JButton queryScoreButton = new JButton("查询");

        courseNoQueryLabel.setFont(new Font("楷体", Font.BOLD, 17));
        queryScoreButton.setFont(new Font("楷体", Font.PLAIN, 17));

        // 设置按钮背景透明
        queryScoreButton.setContentAreaFilled(false);

        courseNoQueryLabel.setBounds(50, 50, 220, 30);
        courseNoQueryField.setBounds(120, 50, 250, 30);
        queryScoreButton.setBounds(100, 150, 80, 30);

        // 已选课程信息表格相关组件
        String[] scoreTitles = {"课程号", "课程名", "成绩"};
        scoreModel = new DefaultTableModel(null, scoreTitles);
        JTable scoreTable = new JTable(scoreModel);
        scoreTable.setRowHeight(20);
        JScrollPane scoreScrollPane = new JScrollPane(scoreTable);
        scoreScrollPane.setPreferredSize(new java.awt.Dimension(550, 100));
        scoreScrollPane.setBounds(50, 200, 550, 220);

        // 查询并显示已选课程信息
        try {
            showSelectedCourses(scoreModel);
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        scoreQueryPanel.setLayout(null);
        scoreQueryPanel.add(courseNoQueryLabel);
        scoreQueryPanel.add(courseNoQueryField);
        scoreQueryPanel.add(queryScoreButton);
        scoreQueryPanel.add(scoreScrollPane);

        // 成绩查询按钮点击事件处理
        queryScoreButton.addActionListener(e -> {
            String courseNo = courseNoQueryField.getText().trim();
            if (courseNo.isEmpty()) {
                JOptionPane.showMessageDialog(null, "请输入课程号", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // 检查用户名是否已设置，如果未设置则提示用户登录
                if (LoginFrame.getUsername() == null || LoginFrame.getUsername().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "请先登录", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                showScore(courseNo, scoreModel);
            } catch (SQLException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });
        return scoreQueryPanel;
    }

    // 展示个人信息
    private void showPersonalInfo(DefaultTableModel model) throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        if (conn == null) {
            System.out.println("数据库连接失败");
            JOptionPane.showMessageDialog(null, "数据库连接失败，请检查网络连接或联系管理员", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Step 1: 从 Users 表中查询学号
            String sno = null;
            String getSnoSql = "SELECT Sno FROM dbo.Users WHERE ID =?";
            try (PreparedStatement getSnoStmt = conn.prepareStatement(getSnoSql)) {
                getSnoStmt.setString(1, LoginFrame.username);
                try (ResultSet snoRs = getSnoStmt.executeQuery()) {
                    if (snoRs.next()) {
                        sno = snoRs.getString("Sno"); // 获取学号
                        System.out.println("找到学号：" + sno);
                    } else {
                        System.out.println("未找到对应的学号");
                        JOptionPane.showMessageDialog(null, "未找到对应的学生信息", "提示", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
            }

            // Step 2: 使用学号从 Student 表查询学生详细信息
            String studentSql = "SELECT Sno, Sname, Ssex, Sage, Sdept FROM dbo.Student WHERE Sno =?";
            try (PreparedStatement studentStmt = conn.prepareStatement(studentSql)) {
                studentStmt.setString(1, sno);
                try (ResultSet studentRs = studentStmt.executeQuery()) {
                    if (studentRs.next()) {
                        Vector<String> rowData = new Vector<>();
                        rowData.add(studentRs.getString("Sno"));
                        rowData.add(studentRs.getString("Sname"));
                        rowData.add(studentRs.getString("Ssex"));
                        rowData.add(studentRs.getString("Sage"));
                        rowData.add(studentRs.getString("Sdept"));
                        model.addRow(rowData);
                        System.out.println("查询到学生信息：" + studentRs.getString("Sname"));
                    } else {
                        System.out.println("未查询到学生信息");
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "获取个人信息时出错: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        } finally {
            DBUtil.closeResources(conn, null, null);
        }
    }

    // 选课操作
    private boolean selectCourse(String courseNo) throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        if (conn == null) {
            System.out.println("数据库连接失败");
            JOptionPane.showMessageDialog(null, "数据库连接失败，请检查网络连接或联系管理员", "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            // Step 1: 从 Users 表中查询学号
            String sno = null;
            String getSnoSql = "SELECT Sno FROM dbo.Users WHERE ID =?";
            try (PreparedStatement getSnoStmt = conn.prepareStatement(getSnoSql)) {
                getSnoStmt.setString(1, LoginFrame.username);
                try (ResultSet snoRs = getSnoStmt.executeQuery()) {
                    if (snoRs.next()) {
                        sno = snoRs.getString("Sno"); // 获取学号
                        System.out.println("找到学号：" + sno);
                    } else {
                        System.out.println("未找到对应的学号");
                        JOptionPane.showMessageDialog(null, "未找到对应的学生信息", "提示", JOptionPane.WARNING_MESSAGE);
                        return false;
                    }
                }
            }

            // Step 2: 检查课程是否存在
            String checkCourseSql = "SELECT * FROM dbo.Course WHERE Cno =?";
            try (PreparedStatement checkCourseStmt = conn.prepareStatement(checkCourseSql)) {
                checkCourseStmt.setString(1, courseNo);
                try (ResultSet courseRs = checkCourseStmt.executeQuery()) {
                    if (!courseRs.next()) {
                        System.out.println("课程号不存在");
                        JOptionPane.showMessageDialog(null, "课程号不存在", "错误", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
            }

            // Step 3: 检查是否已经选过该课程
            String checkSelectedSql = "SELECT * FROM dbo.SC WHERE Sno =? AND Cno =?";
            try (PreparedStatement checkSelectedStmt = conn.prepareStatement(checkSelectedSql)) {
                checkSelectedStmt.setString(1, sno);
                checkSelectedStmt.setString(2, courseNo);
                try (ResultSet selectedRs = checkSelectedStmt.executeQuery()) {
                    if (selectedRs.next()) {
                        System.out.println("已经选过该课程");
                        JOptionPane.showMessageDialog(null, "已选过该课程", "提示", JOptionPane.WARNING_MESSAGE);
                        return false;
                    }
                }
            }

            // Step 4: 插入选课记录
            String insertSql = "INSERT INTO dbo.SC (Sno, Cno) VALUES (?,?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setString(1, sno);
                insertStmt.setString(2, courseNo);
                int result = insertStmt.executeUpdate();
                if (result == 1) {
                    JOptionPane.showMessageDialog(null, "选课成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "选课失败，未知错误，请联系管理员", "错误", JOptionPane.ERROR_MESSAGE);
                }
                return result == 1;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "选课过程中出现错误: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            DBUtil.closeResources(conn, null, null);
        }
    }

    // 展示成绩
    private void showScore(String courseNo, DefaultTableModel model) throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        if (conn == null) {
            System.out.println("数据库连接失败");
            JOptionPane.showMessageDialog(null, "数据库连接失败，请检查网络连接或联系管理员", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Step 1: 从 Users 表中查询学号
            String sno = null;
            String getSnoSql = "SELECT Sno FROM dbo.Users WHERE ID =?";
            try (PreparedStatement getSnoStmt = conn.prepareStatement(getSnoSql)) {
                getSnoStmt.setString(1, LoginFrame.username);
                try (ResultSet snoRs = getSnoStmt.executeQuery()) {
                    if (snoRs.next()) {
                        sno = snoRs.getString("Sno"); // 获取学号
                        System.out.println("找到学号：" + sno);
                    } else {
                        System.out.println("未找到对应的学号");
                        JOptionPane.showMessageDialog(null, "未找到对应的学生信息", "提示", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
            }

            // Step 2: 清空成绩表格
            model.setRowCount(0);

            // Step 3: 查询成绩
            String scoreSql = "SELECT SC.Cno, Course.Cname, SC.Grade, SC.LEVEL " +
                    "FROM dbo.SC " +
                    "JOIN dbo.Course ON SC.Cno = Course.Cno " +
                    "WHERE SC.Sno =? AND SC.Cno =?";
            try (PreparedStatement scoreStmt = conn.prepareStatement(scoreSql)) {
                scoreStmt.setString(1, sno);
                scoreStmt.setString(2, courseNo);
                try (ResultSet rs = scoreStmt.executeQuery()) {
                    if (rs.next()) {
                        Vector<String> rowData = new Vector<>();
                        rowData.add(rs.getString("Cno"));
                        rowData.add(rs.getString("Cname"));
                        rowData.add(rs.getString("Grade"));
                        rowData.add(rs.getString("LEVEL"));
                        model.addRow(rowData);
                        System.out.println("查询到成绩信息：" + rs.getString("Grade"));
                    } else {
                        System.out.println("未查询到该课程的成绩");
                        JOptionPane.showMessageDialog(null, "未找到该课程的成绩", "提示", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "查询成绩时出错: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        } finally {
            DBUtil.closeResources(conn, null, null);
        }
    }

    // 展示所有课程
    private void showAllCourses(DefaultTableModel model) throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        if (conn == null) {
            System.out.println("数据库连接失败");
            JOptionPane.showMessageDialog(null, "数据库连接失败，请检查网络连接或联系管理员", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String courseSql = "SELECT Cno, Cname, Ccredit FROM dbo.Course";
            try (PreparedStatement courseStmt = conn.prepareStatement(courseSql)) {
                try (ResultSet courseRs = courseStmt.executeQuery()) {
                    while (courseRs.next()) {
                        Vector<String> rowData = new Vector<>();
                        rowData.add(courseRs.getString("Cno"));
                        rowData.add(courseRs.getString("Cname"));
                        rowData.add(courseRs.getString("Ccredit"));
                        model.addRow(rowData);
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "获取课程信息时出错: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        } finally {
            DBUtil.closeResources(conn, null, null);
        }
    }

    // 展示已选课程
    private void showSelectedCourses(DefaultTableModel model) throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        if (conn == null) {
            System.out.println("数据库连接失败");
            JOptionPane.showMessageDialog(null, "数据库连接失败，请检查网络连接或联系管理员", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // 从 Users 表中查询学号
            String sno = null;
            String getSnoSql = "SELECT Sno FROM dbo.Users WHERE ID =?";
            try (PreparedStatement getSnoStmt = conn.prepareStatement(getSnoSql)) {
                getSnoStmt.setString(1, LoginFrame.username);
                try (ResultSet snoRs = getSnoStmt.executeQuery()) {
                    if (snoRs.next()) {
                        sno = snoRs.getString("Sno");
                    } else {
                        System.out.println("未找到对应的学号");
                        JOptionPane.showMessageDialog(null, "未找到对应的学生信息", "提示", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
            }

            String selectedCourseSql = "SELECT SC.Cno, Course.Cname, SC.Grade, SC.LEVEL " +
                    "FROM dbo.SC " +
                    "JOIN dbo.Course ON SC.Cno = Course.Cno " +
                    "WHERE SC.Sno =?";
            try (PreparedStatement selectedCourseStmt = conn.prepareStatement(selectedCourseSql)) {
                selectedCourseStmt.setString(1, sno);
                try (ResultSet selectedCourseRs = selectedCourseStmt.executeQuery()) {
                    while (selectedCourseRs.next()) {
                        Vector<String> rowData = new Vector<>();
                        rowData.add(selectedCourseRs.getString("Cno"));
                        rowData.add(selectedCourseRs.getString("Cname"));
                        rowData.add(selectedCourseRs.getString("Grade"));
                        rowData.add(selectedCourseRs.getString("LEVEL"));
                        model.addRow(rowData);
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "获取已选课程信息时出错: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        } finally {
            DBUtil.closeResources(conn, null, null);
        }
    }
}