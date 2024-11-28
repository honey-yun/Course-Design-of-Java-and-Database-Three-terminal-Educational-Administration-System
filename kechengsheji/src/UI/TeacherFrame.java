package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;
import server.DBUtil;

import static UI.LoginFrame.username;

public class TeacherFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private DefaultTableModel personalInfoModel;
    private DefaultTableModel courseModel;
    private DefaultTableModel studentModel;

    // 构造函数，接收教师用户名
    public TeacherFrame() {
        super("教师界面");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        tabbedPane = new JTabbedPane(JTabbedPane.LEFT);

        // 创建并添加个人信息面板
        JPanel personalInfoPanel = createPersonalInfoPanel(username);
        tabbedPane.add("个人信息", personalInfoPanel);

        // 创建并添加课程管理面板
        JPanel courseManagementPanel = createCourseManagementPanel(username);
        tabbedPane.add("课程管理", courseManagementPanel);

        // 创建并添加学生成绩管理面板
        JPanel studentManagementPanel = createStudentManagementPanel(username);
        tabbedPane.add("学生成绩", studentManagementPanel);

        add(tabbedPane);
    }

    // 创建个人信息面板
    private JPanel createPersonalInfoPanel(String username) {
        JPanel personalInfoPanel = new JPanel();
        personalInfoPanel.setLayout(new BorderLayout());

        // 创建表格模型并设置列标题
        String[] columnNames = {"姓名", "性别", "年龄", "职称"};
        personalInfoModel = new DefaultTableModel(null, columnNames);
        JTable personalInfoTable = new JTable(personalInfoModel);
        personalInfoTable.setRowHeight(20);
        personalInfoTable.setFillsViewportHeight(true); // 让表格填满整个可用空间

        // 创建滚动面板
        JScrollPane personalInfoScrollPane = new JScrollPane(personalInfoTable);
        personalInfoPanel.add(personalInfoScrollPane, BorderLayout.CENTER);

        // 加载教师的个人信息
        try {
            loadPersonalInfo(username);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return personalInfoPanel;
    }

    // 加载教师个人信息
    private void loadPersonalInfo(String username) throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        String tno = getTnoFromUsername(username, conn);  // 获取教师的Tno

        String sql = "SELECT Tname, Tsex, Tage, Ttitle FROM Teacher WHERE Tno = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tno);  // 使用教师工号Tno查询个人信息
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Vector<String> rowData = new Vector<>();
                    rowData.add(rs.getString("Tname"));
                    rowData.add(rs.getString("Tsex"));
                    rowData.add(rs.getString("Tage"));
                    rowData.add(rs.getString("Ttitle"));
                    personalInfoModel.addRow(rowData);
                }
            }
        } finally {
            DBUtil.closeResources(conn, null, null);
        }
    }

    // 根据用户名获取工号
    private String getTnoFromUsername(String username, Connection conn) throws SQLException {
        String sql = "SELECT Tno FROM Users WHERE ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Tno");
                }
            }
        }
        return null;  // 若未找到，返回null
    }

    // 创建课程管理面板
    private JPanel createCourseManagementPanel(String username) {
        JPanel courseManagementPanel = new JPanel();
        courseManagementPanel.setLayout(new BorderLayout());

        // 创建课程表格模型并设置列标题
        String[] columnNames = {"课程号", "课程名", "学分"};
        courseModel = new DefaultTableModel(null, columnNames);
        JTable courseTable = new JTable(courseModel);
        courseTable.setRowHeight(20);
        courseTable.setFillsViewportHeight(true); // 让表格填满整个可用空间

        // 创建滚动面板
        JScrollPane courseScrollPane = new JScrollPane(courseTable);
        courseManagementPanel.add(courseScrollPane, BorderLayout.CENTER);

        // 加载教师的课程
        try {
            loadCourses(username);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return courseManagementPanel;
    }

    // 加载教师的课程
    private void loadCourses(String username) throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        String tno = getTnoFromUsername(username, conn);  // 获取教师的Tno

        String sql = "SELECT Cno, Cname, Ccredit FROM Course WHERE Tno = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tno);  // 使用教师工号Tno查询课程
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Vector<String> rowData = new Vector<>();
                    rowData.add(rs.getString("Cno"));
                    rowData.add(rs.getString("Cname"));
                    rowData.add(rs.getString("Ccredit"));
                    courseModel.addRow(rowData);
                }
            }
        } finally {
            DBUtil.closeResources(conn, null, null);
        }
    }

    // 创建学生成绩管理面板
    private JPanel createStudentManagementPanel(String username) {
        JPanel studentManagementPanel = new JPanel();
        studentManagementPanel.setLayout(new BorderLayout());

        // 创建学生成绩表格模型并设置列标题
        String[] columnNames = {"学号", "姓名", "课程号", "课程名", "成绩"};
        studentModel = new DefaultTableModel(null, columnNames);
        JTable studentTable = new JTable(studentModel);
        studentTable.setRowHeight(20);
        studentTable.setFillsViewportHeight(true); // 让表格填满整个可用空间

        // 创建滚动面板
        JScrollPane studentScrollPane = new JScrollPane(studentTable);
        studentManagementPanel.add(studentScrollPane, BorderLayout.CENTER);

        // 添加按钮功能
        JPanel buttonPanel = new JPanel();
        JButton editGradeButton = new JButton("修改成绩");
        editGradeButton.addActionListener(e -> editStudentGrade(studentTable));
        buttonPanel.add(editGradeButton);

        studentManagementPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 加载学生成绩
        try {
            loadStudentGrades(username);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return studentManagementPanel;
    }

    // 加载学生成绩
    private void loadStudentGrades(String username) throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        String tno = getTnoFromUsername(username, conn);  // 获取教师的Tno

        studentModel.setRowCount(0); // 先清空表格数据

        String sql = "SELECT Student.Sno, Student.Sname, SC.Cno, Course.Cname, SC.Grade " +
                "FROM SC JOIN Student ON SC.Sno = Student.Sno " +
                "JOIN Course ON SC.Cno = Course.Cno " +
                "WHERE Course.Tno =?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tno);  // 使用教师工号Tno查询学生成绩
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Vector<String> rowData = new Vector<>();
                    rowData.add(rs.getString("Sno"));
                    rowData.add(rs.getString("Sname"));
                    rowData.add(rs.getString("Cno"));
                    rowData.add(rs.getString("Cname"));
                    rowData.add(rs.getString("Grade"));
                    studentModel.addRow(rowData);
                }
            }
        } finally {
            DBUtil.closeResources(conn, null, null);
        }
    }
    // 修改成绩功能
    private void editStudentGrade(JTable studentTable) {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow!= -1) {
            // 获取当前选择的学生成绩信息
            String studentNo = (String) studentModel.getValueAt(selectedRow, 0);
            String courseNo = (String) studentModel.getValueAt(selectedRow, 2);
            String currentGrade = (String) studentModel.getValueAt(selectedRow, 4);

            // 弹出对话框让用户输入新的成绩
            String newGrade = JOptionPane.showInputDialog(this,
                    "修改成绩：请输入新的成绩", currentGrade);

            if (newGrade!= null &&!newGrade.isEmpty()) {
                // 执行修改成绩操作
                try {
                    updateGrade(studentNo, courseNo, newGrade);
                    // 成功修改后刷新表格
                    loadStudentGrades(username);
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this, "成绩不能为空", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "请选择一条成绩记录进行修改", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 更新成绩到数据库
    private void updateGrade(String studentNo, String courseNo, String newGrade) throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        String sql = "UPDATE SC SET Grade =? WHERE Sno =? AND Cno =?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newGrade);
            stmt.setString(2, studentNo);
            stmt.setString(3, courseNo);
            stmt.executeUpdate();
        } finally {
            DBUtil.closeResources(conn, null, null);
        }
    }

    // 刷新学生成绩
    private void refreshStudentGrades() throws SQLException, ClassNotFoundException {
        studentModel.setRowCount(0); // 清空表格
        loadStudentGrades(username);
    }
}