package UI;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import server.DBUtil;

public class AdminFrame extends JFrame {
    private DefaultTableModel studentModel;
    private DefaultTableModel courseModel;
    private DefaultTableModel scModel;
    private DefaultTableModel userModel;
    private DefaultTableModel teacherModel;

    public AdminFrame() {
        super("管理员界面");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null);
        setVisible(true);

        // 左侧操作按钮面板
        JPanel leftPanel = new JPanel();
        JButton addButton = new JButton("添加");
        JButton updateButton = new JButton("修改");
        JButton deleteButton = new JButton("删除");
        JButton queryButton = new JButton("查询");
        JButton showAllButton = new JButton("全部信息展示");

        addButton.setFont(new Font("楷体", Font.PLAIN, 17));
        updateButton.setFont(new Font("楷体", Font.PLAIN, 17));
        deleteButton.setFont(new Font("楷体", Font.PLAIN, 17));
        queryButton.setFont(new Font("楷体", Font.PLAIN, 17));
        showAllButton.setFont(new Font("楷体", Font.BOLD, 17));

        // 设置按钮背景透明
        addButton.setContentAreaFilled(false);
        updateButton.setContentAreaFilled(false);
        deleteButton.setContentAreaFilled(false);
        queryButton.setContentAreaFilled(false);
        showAllButton.setContentAreaFilled(false);

        JLabel inputLabel = new JLabel("请输入学号:");
        inputLabel.setFont(new Font("楷体", Font.PLAIN, 17));
        JTextField inputField = new JTextField(20);

        leftPanel.setLayout(null);
        addButton.setBounds(20, 30, 70, 30);
        updateButton.setBounds(105, 30, 70, 30);
        deleteButton.setBounds(20, 100, 70, 30);
        queryButton.setBounds(105, 100, 70, 30);
        inputLabel.setBounds(395, 30, 100, 25);
        inputField.setBounds(500, 30, 200, 25);
        showAllButton.setBounds(460, 85, 210, 30);

        leftPanel.add(addButton);
        leftPanel.add(updateButton);
        leftPanel.add(deleteButton);
        leftPanel.add(queryButton);
        leftPanel.add(inputLabel);
        leftPanel.add(inputField);
        leftPanel.add(showAllButton);

        // 右侧信息表格面板
        JPanel rightPanel = new JPanel();
        String[] studentTitles = {"学号", "姓名", "性别", "年龄", "专业"};
        String[] courseTitles = {"课程号", "课程名", "学分","工号"};
        String[] scTitles = {"学号", "课程号", "成绩"};
        String[] userTitles = {"账号", "密码", "学号", "工号", "电话号码", "角色"};
        String[] teacherTitles = {"教师工号", "教师姓名", "教师性别", "教师年龄", "教师职称", "手机号"};

        studentModel = new DefaultTableModel(null, studentTitles);
        userModel = new DefaultTableModel(null, userTitles);
        scModel = new DefaultTableModel(null, scTitles);
        courseModel = new DefaultTableModel(null, courseTitles);
        teacherModel = new DefaultTableModel(null, teacherTitles);

        JTable studentTable = new JTable(studentModel);
        JTable courseTable = new JTable(courseModel);
        JTable scTable = new JTable(scModel);
        JTable userTable = new JTable(userModel);
        JTable teacherTable = new JTable(teacherModel);

        JScrollPane studentScrollPane = new JScrollPane(studentTable);
        JScrollPane courseScrollPane = new JScrollPane(courseTable);
        JScrollPane scScrollPane = new JScrollPane(scTable);
        JScrollPane userScrollPane = new JScrollPane(userTable);
        JScrollPane teacherScrollPane = new JScrollPane(teacherTable);

        studentScrollPane.setPreferredSize(new java.awt.Dimension(600, 100));
        courseScrollPane.setPreferredSize(new java.awt.Dimension(600, 100));
        scScrollPane.setPreferredSize(new java.awt.Dimension(600, 100));
        userScrollPane.setPreferredSize(new java.awt.Dimension(600, 100));
        teacherScrollPane.setPreferredSize(new java.awt.Dimension(600, 100));

        studentScrollPane.setBounds(170, 0, 600, 100);
        courseScrollPane.setBounds(170, 110, 600, 100);
        scScrollPane.setBounds(170, 220, 600, 100);
        userScrollPane.setBounds(170, 330, 600, 100);
        teacherScrollPane.setBounds(170, 440, 600, 100);

        rightPanel.setLayout(null);
        rightPanel.add(studentScrollPane);
        rightPanel.add(courseScrollPane);
        rightPanel.add(scScrollPane);
        rightPanel.add(userScrollPane);
        rightPanel.add(teacherScrollPane);

        // 使用JSplitPane分割左右面板
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(150);
        add(splitPane, BorderLayout.CENTER);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 跳转到添加信息界面
                new AddFrame().setVisible(true);
            }
        });

        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 跳转到修改信息界面
                new UpdateFrame().setVisible(true);
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 跳转到删除信息界面
                new DeleteFrame().setVisible(true);
            }
        });

        queryButton.addActionListener(e -> {
            String sno = inputField.getText().trim();
            try {
                queryData(sno);
            } catch (SQLException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });

        showAllButton.addActionListener(e -> {
            try {
                showAllData();
            } catch (SQLException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });
    }

    // 查询数据并更新表格的方法
    private void queryData(String sno) throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, "数据库连接失败，请检查配置或联系管理员", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 验证学号格式是否为数字
        if (!sno.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "学号必须为数字，请重新输入", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 清空查询相关表格数据
        clearRelatedTables(sno);

        // 查询学生信息
        String studentSql = "SELECT * FROM dbo.Student WHERE Sno =?";
        PreparedStatement studentPstmt = conn.prepareStatement(studentSql);
        studentPstmt.setString(1, sno);
        ResultSet studentRs = studentPstmt.executeQuery();
        PreparedStatement userPstmt = null;
        ResultSet userRs = null;
        if (studentRs.next()) {
            Vector<String> studentRow = new Vector<>();
            studentRow.addElement(studentRs.getString("Sno"));
            studentRow.addElement(studentRs.getString("Sname"));
            studentRow.addElement(studentRs.getString("Ssex"));
            studentRow.addElement(studentRs.getString("Sage"));
            studentRow.addElement(studentRs.getString("Sdept"));
            studentModel.addRow(studentRow);

            // 查询用户信息
            String userSql = "SELECT * FROM dbo.Users WHERE Sno =? OR Tno =?";
            userPstmt = conn.prepareStatement(userSql);
            userPstmt.setString(1, sno);
            userPstmt.setString(2, sno);
            userRs = userPstmt.executeQuery();
            if (userRs.next()) {
                Vector<String> userRow = new Vector<>();
                userRow.addElement(userRs.getString("ID"));
                userRow.addElement(userRs.getString("PAWD"));
                userRow.addElement(userRs.getString("Sno"));
                userRow.addElement(userRs.getString("Tno"));
                userRow.addElement(userRs.getString("phone"));
                userRow.addElement(userRs.getString("role"));
                userModel.addRow(userRow);
            }
        }

        // 查询选课信息
        String scSql = "SELECT * FROM dbo.SC WHERE Sno =?";
        PreparedStatement scPstmt = conn.prepareStatement(scSql);
        scPstmt.setString(1, sno);
        ResultSet scRs = scPstmt.executeQuery();
        PreparedStatement coursePstmt = null;
        ResultSet courseRs = null;
        while (scRs.next()) {
            Vector<String> scRow = new Vector<>();
            scRow.addElement(scRs.getString("Sno"));
            scRow.addElement(scRs.getString("Cno"));
            scRow.addElement(scRs.getString("Grade"));
            scModel.addRow(scRow);

            // 查询课程信息
            String courseSql = "SELECT * FROM dbo.Course WHERE Cno =?";
            coursePstmt = conn.prepareStatement(courseSql);
            coursePstmt.setString(1, scRs.getString("Cno"));
            courseRs = coursePstmt.executeQuery();
            if (courseRs.next()) {
                Vector<String> courseRow = new Vector<>();
                courseRow.addElement(courseRs.getString("Cno"));
                courseRow.addElement(courseRs.getString("Cname"));
                courseRow.addElement(courseRs.getString("Ccredit"));
                courseModel.addRow(courseRow);
            }
        }

        DBUtil.closeResources(conn, studentPstmt, studentRs);
        DBUtil.closeResources(conn, userPstmt, userRs);
        DBUtil.closeResources(conn, scPstmt, scRs);
        DBUtil.closeResources(conn, coursePstmt, courseRs);
    }

    // 显示所有数据的方法
    private void showAllData() throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, "数据库连接失败，请检查配置或联系管理员", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 清空所有表格数据
        clearAllTables();

        // 查询学生信息
        String studentSql = "SELECT * FROM dbo.Student";
        PreparedStatement studentPstmt = conn.prepareStatement(studentSql);
        ResultSet studentRs = studentPstmt.executeQuery();
        while (studentRs.next()) {
            Vector<String> studentRow = new Vector<>();
            studentRow.addElement(studentRs.getString("Sno"));
            studentRow.addElement(studentRs.getString("Sname"));
            studentRow.addElement(studentRs.getString("Ssex"));
            studentRow.addElement(studentRs.getString("Sage"));
            studentRow.addElement(studentRs.getString("Sdept"));
            studentModel.addRow(studentRow);
        }

        // 查询课程信息
        String courseSql = "SELECT * FROM dbo.Course";
        PreparedStatement coursePstmt = conn.prepareStatement(courseSql);
        ResultSet courseRs = coursePstmt.executeQuery();
        while (courseRs.next()) {
            Vector<String> courseRow = new Vector<>();
            // 确保正确获取课程号并添加到表格模型
            courseRow.addElement(courseRs.getString("Cno"));
            courseRow.addElement(courseRs.getString("Cname"));
            courseRow.addElement(courseRs.getString("Ccredit"));
            courseRow.addElement(courseRs.getString("Tno"));
            courseModel.addRow(courseRow);
        }

        // 查询选课信息
        String scSql = "SELECT sc.Sno, sc.Cno, sc.Grade, c.Cname " +
                "FROM dbo.SC sc " +
                "JOIN dbo.Course c ON sc.Cno = c.Cno";
        PreparedStatement scPstmt = conn.prepareStatement(scSql);
        ResultSet scRs = scPstmt.executeQuery();
        while (scRs.next()) {
                Vector<String> scRow = new Vector<>();
                scRow.addElement(scRs.getString("Sno"));
                scRow.addElement(scRs.getString("Cno"));
                scRow.addElement(scRs.getString("Grade"));
                scRow.addElement(scRs.getString("Cname"));
                scModel.addRow(scRow);
            }

            // 查询用户信息
            String userSql = "SELECT * FROM dbo.Users";
            PreparedStatement userPstmt = conn.prepareStatement(userSql);
        ResultSet userRs = userPstmt.executeQuery();
        while (userRs.next()) {
            Vector<String> userRow = new Vector<>();
            userRow.addElement(userRs.getString("ID"));
            userRow.addElement(userRs.getString("PAWD"));
            userRow.addElement(userRs.getString("Sno"));
            userRow.addElement(userRs.getString("Tno"));
            userRow.addElement(userRs.getString("phone"));
            userRow.addElement(userRs.getString("role"));
            userModel.addRow(userRow);
        }
        // 查询教师信息
        String teacherSql = "SELECT * FROM dbo.Teacher";
        PreparedStatement teacherPstmt = conn.prepareStatement(teacherSql);
        ResultSet teacherRs = teacherPstmt.executeQuery();
        while (teacherRs.next()) {
            Vector<String> teacherRow = new Vector<>();
            teacherRow.addElement(teacherRs.getString("Tno"));
            teacherRow.addElement(teacherRs.getString("Tname"));
            teacherRow.addElement(teacherRs.getString("Tsex"));
            teacherRow.addElement(teacherRs.getString("Tage"));
            teacherRow.addElement(teacherRs.getString("Ttitle"));
            teacherRow.addElement(teacherRs.getString("phone"));
            teacherModel.addRow(teacherRow);
        }

        // 关闭数据库资源
        DBUtil.closeResources(conn, studentPstmt, studentRs);
        DBUtil.closeResources(conn, coursePstmt, courseRs);
        DBUtil.closeResources(conn, scPstmt, scRs);
        DBUtil.closeResources(conn, userPstmt, userRs);
    }

    // 清空所有表格数据的方法
    private void clearAllTables() {
        studentModel.setRowCount(0);
        courseModel.setRowCount(0);
        scModel.setRowCount(0);
        userModel.setRowCount(0);
        teacherModel.setRowCount(0);
    }

    // 清空查询相关表格数据的方法
    private void clearRelatedTables(String sno) {
        studentModel.setRowCount(0);
        courseModel.setRowCount(0);
        scModel.setRowCount(0);
        userModel.setRowCount(0);
        teacherModel.setRowCount(0);
    }
}
