/***********************************************************
 * 版权所有 (C)2024, Lishiyun
 *
 * 文件名称： AddStudentFrame.java
 * 文件标识：无
 * 内容摘要：用于管理员端添加学生信息操作
 * 其它说明：
 * 当前版本： V1.0
 * 作   者：李世赟
 * 完成日期： 20241130
 **********************************************************/
package UI;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import server.DBUtil;

public class AddStudentFrame extends JFrame {
    /*********************************************************
     * 功能描述：构造函数，用于初始化添加学生信息的图形用户界面框架
     * 输入参数：无
     * 返回值：无
     * 其它说明：设置框架标题、关闭操作、大小和位置，并使其可见。创建包含学生信息输入组件和按钮的面板，设置组件字体、布局及按钮背景透明等样式，为按钮添加事件监听器，用于处理添加学生信息和重置输入框操作。
     ************************************************************/
    public AddStudentFrame() {
        super("添加学生信息");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 550);
        setLocationRelativeTo(null);
        setVisible(true);

        JPanel frame = new JPanel();
        add(frame);

        // 学生信息标签和文本框
        JLabel j = new JLabel("学号:");
        JLabel j1 = new JLabel("姓名:");
        JLabel j2 = new JLabel("性别:");
        JLabel j3 = new JLabel("年龄:");
        JLabel j4 = new JLabel("专业:");

        j.setFont(new Font("楷体", Font.PLAIN, 20));
        j1.setFont(new Font("楷体", Font.PLAIN, 20));
        j2.setFont(new Font("楷体", Font.PLAIN, 20));
        j3.setFont(new Font("楷体", Font.PLAIN, 20));
        j4.setFont(new Font("楷体", Font.PLAIN, 20));

        JTextField c = new JTextField(15);
        JTextField c1 = new JTextField(15);
        JTextField c2 = new JTextField(15);
        JTextField c3 = new JTextField(15);
        JTextField c4 = new JTextField(15);

        JButton aa = new JButton("确定");
        JButton bb = new JButton("重置");

        aa.setFont(new Font("楷体", Font.PLAIN, 20));
        bb.setFont(new Font("楷体", Font.PLAIN, 20));

        // 设置按钮背景透明
        aa.setContentAreaFilled(false);
        bb.setContentAreaFilled(false);

        // 学生信息组件布局
        j.setBounds(20, 30, 50, 20);
        c.setBounds(80, 30, 120, 25);
        j1.setBounds(20, 70, 50, 20);
        c1.setBounds(80, 70, 100, 25);
        j2.setBounds(20, 110, 50, 30);
        c2.setBounds(80, 110, 100, 25);
        j3.setBounds(20, 150, 50, 30);
        c3.setBounds(80, 150, 100, 25);
        j4.setBounds(20, 190, 50, 30);
        c4.setBounds(80, 190, 100, 25);

        aa.setBounds(100, 400, 100, 30);
        bb.setBounds(300, 400, 100, 30);

        frame.setLayout(null);
        frame.add(j);
        frame.add(c);
        frame.add(j1);
        frame.add(c1);
        frame.add(j2);
        frame.add(c2);
        frame.add(j3);
        frame.add(c3);
        frame.add(j4);
        frame.add(c4);
        frame.add(aa);
        frame.add(bb);

        /*********************************************************
         * 功能描述：“确定”按钮的点击事件处理方法
         * 输入参数：e - 动作事件对象，包含按钮点击的相关信息
         * 返回值：无
         * 其它说明：当点击“确定”按钮时，获取学生信息文本框中的内容，调用 addStudent 方法尝试将学生信息插入数据库。若插入成功，显示提示信息；若插入失败（可能因学号已存在），显示错误信息。若在数据库操作过程中出现 SQL 异常或类加载异常，将打印堆栈跟踪信息。
         ************************************************************/
        aa.addActionListener(e -> {
            String sno = c.getText().trim();
            String sname = c1.getText().trim();
            String ssex = c2.getText().trim();
            String sage = c3.getText().trim();
            String sdept = c4.getText().trim();

            try {
                if (addStudent(sno, sname, ssex, sage, sdept)) {
                    JOptionPane.showMessageDialog(null, "添加学生信息成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "添加学生信息失败，可能学号已存在", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });
        /*********************************************************
         * 功能描述：“重置”按钮的点击事件处理方法
         * 输入参数：e - 动作事件对象，包含按钮点击的相关信息
         * 返回值：无
         * 其它说明：当点击“重置”按钮时，清空所有学生信息文本框中的内容。
         ************************************************************/
        bb.addActionListener(e -> {
            c.setText("");
            c1.setText("");
            c2.setText("");
            c3.setText("");
            c4.setText("");
        });
    }
    /*********************************************************
     * 功能描述：将学生信息插入数据库的方法
     * 输入参数：
     * - sno：学号
     * - sname：姓名
     * - ssex：性别
     * - sage：年龄
     * - sdept：专业
     * 返回值：true - 插入成功；false - 插入失败（如因 SQL 异常或学号已存在等原因）
     * 其它说明：首先获取数据库连接，然后构建插入学生信息的 SQL 语句并预编译。将学生信息参数设置到预编译语句中，执行更新操作并根据影响行数判断插入是否成功。若成功则关闭数据库资源并返回 true；若失败（如出现 SQL 错误），显示错误信息并返回 false。若在获取连接或执行 SQL 操作过程中出现异常，将异常信息打印堆栈跟踪。
     ************************************************************/
    private boolean addStudent(String sno, String sname, String ssex, String sage, String sdept) throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        String sql = "INSERT INTO dbo.Student (Sno, Sname, Ssex, Sage, Sdept) VALUES (?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, sno);
        pstmt.setString(2, sname);
        pstmt.setString(3, ssex);
        pstmt.setString(4, sage);
        pstmt.setString(5, sdept);
        try {
            int result = pstmt.executeUpdate();
            DBUtil.closeResources(conn, pstmt, null);
            return result == 1;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "添加学生信息失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}