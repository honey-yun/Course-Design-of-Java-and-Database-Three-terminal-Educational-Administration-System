/***********************************************************
 * 版权所有 (C)2024, Lishiyun
 *
 * 文件名称： StudentFrame.java
 * 文件标识：无
 * 内容摘要：学生端主界面UI，学生所需的各个功能的具体实现
 * 其它说明：
 * 当前版本： V1.0
 * 作   者：李世赟
 * 完成日期： 20241130
 **********************************************************/
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
    /*********************************************************
     * 功能描述：构造函数，用于初始化学生界面框架
     * 输入参数：无
     * 返回值：无
     * 其它说明：设置框架标题、关闭操作、大小和位置，并使其可见。创建选项卡窗格，分别添加个人信息、选课、成绩查询选项卡面板，通过不同面板为学生提供查看个人信息、选课操作及成绩查询功能入口，构建完整学生端交互框架。
     ************************************************************/
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
    /*********************************************************
     * 功能描述：创建个人信息选项卡面板的方法
     * 输入参数：无
     * 返回值：返回构建好的个人信息选项卡面板；若未登录则提示错误并关闭当前框架后返回 null
     * 其它说明：构建面板，设表格模型与列标题展示个人信息。若用户名未设置（未登录），提示错误并关闭框架；否则调用 showPersonalInfo 填充表格，添加滚动面板并布局组件，为学生呈现个人信息展示区域及数据加载逻辑。
     ************************************************************/
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

    /*********************************************************
     * 功能描述：创建选课选项卡面板的方法
     * 输入参数：无
     * 返回值：返回构建好的选课选项卡面板
     * 其它说明：构建面板，设标签、文本框与选课按钮及课程表格模型展示课程信息。查询并填充课程表，为选课按钮绑定监听器，实现选课验证、数据库交互及结果提示功能，包括课程号存在与唯一性校验、选课记录插入及错误处理，构建选课交互流程。
     ************************************************************/
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
    /*********************************************************
     * 功能描述：创建成绩查询选项卡面板的方法
     * 输入参数：无
     * 返回值：返回构建好的成绩查询选项卡面板；若未登录则提示错误并返回
     * 其它说明：构建面板，设标签、文本框与查询按钮及成绩表格模型。若未登录提示错误返回；否则为查询按钮绑定监听器，实现成绩查询逻辑，含用户名校验、成绩表清空与数据填充，依课程号检索并展示成绩，构建成绩查询交互布局。
     ************************************************************/
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
    /*********************************************************
     * 功能描述：展示个人信息的方法
     * 输入参数：model - 用于存储个人信息数据的表格模型
     * 返回值：无
     * 其它说明：获取数据库连接，先从 Users 表查学号，再依学号从 Student 表取详细信息填充模型。过程中处理连接失败、学号未找到等情况，确保准确展示个人信息或给予恰当提示，关闭资源保障数据库资源有效利用。
     ************************************************************/
    private void showPersonalInfo(DefaultTableModel model) throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        if (conn == null) {
            System.out.println("数据库连接失败");
            JOptionPane.showMessageDialog(null, "数据库连接失败，请检查网络连接或联系管理员", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            //从 Users 表中查询学号
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

            // 使用学号从 Student 表查询学生详细信息
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

    /*********************************************************
     * 功能描述：选课操作的方法
     * 输入参数：courseNo - 要选课程的课程号
     * 返回值：true - 选课成功；false - 选课失败（课程号问题或已选等）
     * 其它说明：获取连接后，依次从 Users 表查学号、验证课程存在及是否已选，通过则插入选课记录。依据操作结果提示信息并返回选课状态，遇异常处理并返回 false，维护选课功能稳定性与数据一致性。
     ************************************************************/
    private boolean selectCourse(String courseNo) throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        if (conn == null) {
            System.out.println("数据库连接失败");
            JOptionPane.showMessageDialog(null, "数据库连接失败，请检查网络连接或联系管理员", "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            //  从 Users 表中查询学号
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
            //  检查课程是否存在
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
            //  检查是否已经选过该课程
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
            //  插入选课记录
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
    /*********************************************************
     * 功能描述：展示成绩的方法
     * 输入参数：
     * - courseNo - 要查询成绩的课程号
     * - model - 存储成绩数据的表格模型
     * 返回值：无
     * 其它说明：获取连接，查学号后清空成绩表，依学号课程号检索成绩填充表格。处理连接异常与未查询到成绩情况，关闭资源确保成绩查询功能可靠，为学生提供准确成绩展示。
     ************************************************************/
    private void showScore(String courseNo, DefaultTableModel model) throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        if (conn == null) {
            System.out.println("数据库连接失败");
            JOptionPane.showMessageDialog(null, "数据库连接失败，请检查网络连接或联系管理员", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            //  从 Users 表中查询学号
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
            // 清空成绩表格
            model.setRowCount(0);
            //  查询成绩
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
    /*********************************************************
     * 功能描述：展示所有课程的方法
     * 输入参数：model - 用于存储课程信息的表格模型
     * 返回值：无
     * 其它说明：获取连接后查询所有课程信息填充模型，处理连接失败异常，关闭资源保障课程展示功能正常运行，助学生浏览可选课程列表。
     ************************************************************/
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
    /*********************************************************
     * 功能描述：展示已选课程的方法
     * 输入参数：model - 存储已选课程信息的表格模型
     * 返回值：无
     * 其它说明：获取连接查学号后，依学号检索已选课程信息填充模型，处理学号未找到及查询异常，关闭资源实现已选课程准确呈现，辅助学生查看选课记录。
     ************************************************************/
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