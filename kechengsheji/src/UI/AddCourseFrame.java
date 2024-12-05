/***********************************************************
 * 版权所有 (C)2024, Lishiyun
 *
 * 文件名称： AddCourseFrame.java
 * 文件标识：无
 * 内容摘要：用于管理员端添加课程操作
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

public class AddCourseFrame extends JFrame {
    /*********************************************************
     * 功能描述：构造函数，用于初始化添加课程信息的图形用户界面框架
     * 输入参数：无
     * 返回值：无
     * 其它说明：设置框架标题、关闭操作、大小和位置，并使其可见。同时创建包含课程信息输入组件和按钮的面板，将其添加到框架中，并为按钮添加相应的事件监听器。
     ************************************************************/
    public AddCourseFrame() {
        super("添加课程信息");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 550);
        setLocationRelativeTo(null);
        setVisible(true);

        JPanel frame = new JPanel();
        add(frame);

        // 课程信息标签和文本框
        JLabel j5 = new JLabel("课程号:");
        JLabel j6 = new JLabel("课程名:");
        JLabel j7 = new JLabel("学分:");
        JLabel j8 = new JLabel("教师工号:");

        j5.setFont(new Font("楷体", Font.PLAIN, 20));
        j6.setFont(new Font("楷体", Font.PLAIN, 20));
        j7.setFont(new Font("楷体", Font.PLAIN, 20));
        j8.setFont(new Font("楷体", Font.PLAIN, 20));

        JTextField c5 = new JTextField(15);
        JTextField c6 = new JTextField(15);
        JTextField c7 = new JTextField(15);
        JTextField c8 = new JTextField(15);

        JButton aa = new JButton("确定");
        JButton bb = new JButton("重置");

        aa.setFont(new Font("楷体", Font.PLAIN, 20));
        bb.setFont(new Font("楷体", Font.PLAIN, 20));

        // 设置按钮背景透明
        aa.setContentAreaFilled(false);
        bb.setContentAreaFilled(false);

        // 课程信息组件布局
        j5.setBounds(20, 30, 120, 20);
        c5.setBounds(80, 30, 120, 25);
        j6.setBounds(20, 70, 120, 20);
        c6.setBounds(80, 70, 100, 25);
        j7.setBounds(20, 110, 120, 30);
        c7.setBounds(80, 110, 100, 25);
        j8.setBounds(20, 150, 120, 30);
        c8.setBounds(80, 150, 100, 25);

        aa.setBounds(100, 400, 100, 30);
        bb.setBounds(300, 400, 100, 30);

        frame.setLayout(null);
        frame.add(j5);
        frame.add(c5);
        frame.add(j6);
        frame.add(c6);
        frame.add(j7);
        frame.add(c7);
        frame.add(j8);
        frame.add(c8);
        frame.add(aa);
        frame.add(bb);

        /*********************************************************
         * 功能描述：“确定”按钮的点击事件处理方法
         * 输入参数：e - 动作事件对象，包含按钮点击的相关信息
         * 返回值：无
         * 其它说明：当点击“确定”按钮时，获取课程信息文本框中的内容，调用 addCourse 方法尝试将课程信息插入数据库。若插入成功，显示提示信息；若插入失败（可能因课程号已存在），显示错误信息。若在数据库操作过程中出现 SQL 异常或类加载异常，将打印堆栈跟踪信息。
         ************************************************************/
        aa.addActionListener(e -> {
            String cno = c5.getText().trim();
            String cname = c6.getText().trim();
            String ccredit = c7.getText().trim();
            String tno = c8.getText().trim();

            try {
                if (addCourse(cno, cname, ccredit, tno)) {
                    JOptionPane.showMessageDialog(null, "添加课程信息成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "添加课程信息失败，可能课程号已存在", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });
        /*********************************************************
         * 功能描述：“重置”按钮的点击事件处理方法
         * 输入参数：e - 动作事件对象，包含按钮点击的相关信息
         * 返回值：无
         * 其它说明：当点击“重置”按钮时，清空所有课程信息文本框中的内容。
         ************************************************************/
        bb.addActionListener(e -> {
            c5.setText("");
            c6.setText("");
            c7.setText("");
            c8.setText("");
        });
    }
    /*********************************************************
     * 功能描述：将课程信息插入数据库的方法
     * 输入参数：
     * - cno：课程号
     * - cname：课程名
     * - ccredit：学分
     * - tno：教师工号
     * 返回值：true - 插入成功；false - 插入失败（如因 SQL 异常或课程号已存在等原因）
     * 其它说明：首先获取数据库连接，然后构建插入课程信息的 SQL 语句并预编译。将课程信息参数设置到预编译语句中，执行更新操作并根据影响行数判断插入是否成功。若成功则关闭数据库资源并返回 true；若失败（如出现 SQL 错误），显示错误信息并返回 false。若在获取连接或执行 SQL 操作过程中出现异常，将异常信息打印堆栈跟踪。
     ************************************************************/
    private boolean addCourse(String cno, String cname, String ccredit, String tno) throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        String sql = "INSERT INTO dbo.Course (Cno, Cname, Ccredit, Tno) VALUES (?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, cno);
        pstmt.setString(2, cname);
        pstmt.setString(3, ccredit);
        pstmt.setString(4, tno);
        try {
            int result = pstmt.executeUpdate();
            DBUtil.closeResources(conn, pstmt, null);
            return result == 1;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "添加课程信息失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}