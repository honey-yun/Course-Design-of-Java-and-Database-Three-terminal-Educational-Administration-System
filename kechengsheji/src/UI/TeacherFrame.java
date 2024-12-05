/***********************************************************
 * 版权所有 (C)2024, Lishiyun
 *
 * 文件名称： TeacherFrame.java
 * 文件标识：无
 * 内容摘要：教师端主界面UI，用于实现教师所需的各个功能
 * 其它说明：
 * 当前版本： V1.0
 * 作   者：李世赟
 * 完成日期： 20241130
 **********************************************************/
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
    /*********************************************************
     * 功能描述：构造函数，用于初始化教师界面框架
     * 输入参数：无
     * 返回值：无
     * 其它说明：设置框架标题、关闭操作、大小和位置，并使其可见。创建选项卡窗格，分别添加个人信息、课程管理、学生成绩管理选项卡面板，各面板通过不同方法构建并加载相应数据，为教师提供信息展示与操作入口，构建完整教师端交互框架。
     ************************************************************/
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
    /*********************************************************
     * 功能描述：创建个人信息面板的方法
     * 输入参数：username - 教师用户名，用于查询关联个人信息
     * 返回值：返回构建好的个人信息面板
     * 其它说明：构建面板设布局，创建表格模型与列标题展示个人信息。加载教师信息填充表格，添加滚动面板布局组件，为教师呈现个人信息展示区及数据加载逻辑，依用户名精准提取数据。
     ************************************************************/
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
    /*********************************************************
     * 功能描述：加载教师个人信息的方法
     * 输入参数：username - 教师用户名
     * 返回值：无
     * 其它说明：获取连接，依用户名查工号，再依工号从 Teacher 表取个人信息填充模型。处理连接失败、工号未找到等情况，关闭资源确保信息准确展示或给予提示，保障数据可靠获取与呈现。
     ************************************************************/
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
    /*********************************************************
     * 功能描述：根据用户名获取工号的方法
     * 输入参数：
     * - username - 教师用户名
     * - conn - 数据库连接对象
     * 返回值：返回教师工号；若未找到则返回 null
     * 其它说明：利用传入连接，依用户名从 Users 表查询工号。若结果集有记录则返回工号；否则返回 null，为后续依工号查询信息提供基础数据支持，确保信息关联查询准确性。
     ************************************************************/
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
    /*********************************************************
     * 功能描述：创建课程管理面板的方法
     * 输入参数：username - 教师用户名，用于筛选关联课程
     * 返回值：返回构建好的课程管理面板
     * 其它说明：构建面板设布局，创建课程表模型与列标题。加载教师课程填充表格，添加滚动面板布局组件，为教师提供课程管理展示区与数据加载逻辑，依用户名精准筛选课程数据。
     ************************************************************/
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
    /*********************************************************
     * 功能描述：加载教师课程的方法
     * 输入参数：username - 教师用户名
     * 返回值：无
     * 其它说明：获取连接，依用户名查工号，再依工号从 Course 表取课程信息填充模型。处理连接异常与无课程情况，关闭资源确保课程准确展示，助教师管理授课课程信息。
     ************************************************************/
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
    /*********************************************************
     * 功能描述：加载学生成绩的方法
     * 输入参数：username - 教师用户名
     * 返回值：无
     * 其它说明：获取连接，依用户名查工号，清空成绩表后依工号从多表联合查询学生成绩填充。处理连接异常与无成绩情况，关闭资源确保成绩准确展示，助教师掌握学生学习情况。
     ************************************************************/
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
    /*********************************************************
     * 功能描述：修改成绩功能的方法
     * 输入参数：studentTable - 显示学生成绩的表格，用于获取选中行信息
     * 返回值：无
     * 其它说明：获取选中行成绩信息，弹出对话框让教师输入新成绩。若输入有效，调用 updateGrade 更新数据库成绩并刷新表格；否则提示错误，确保成绩修改操作准确可靠与界面交互流畅。
     ************************************************************/
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
    /*********************************************************
     * 功能描述：更新成绩到数据库的方法
     * 输入参数：
     * - studentNo - 学生学号
     * - courseNo - 课程号
     * - newGrade - 新成绩
     * 返回值：无
     * 其它说明：获取连接，构建更新成绩 SQL 语句设参执行更新，关闭资源确保数据库成绩更新成功，维护成绩数据一致性与准确性，响应教师修改成绩操作。
     ************************************************************/
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
    /*********************************************************
     * 功能描述：刷新学生成绩的方法
     * 输入参数：无
     * 返回值：无
     * 其它说明：清空成绩表格后调用 loadStudentGrades 重新加载成绩数据，确保教师获取最新成绩信息，提升数据实时性与交互体验，如应对成绩修改后即时刷新展示需求。
     ************************************************************/
    private void refreshStudentGrades() throws SQLException, ClassNotFoundException {
        studentModel.setRowCount(0); // 清空表格
        loadStudentGrades(username);
    }
}