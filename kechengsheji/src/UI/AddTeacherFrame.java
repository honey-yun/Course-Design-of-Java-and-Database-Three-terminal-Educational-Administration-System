/***********************************************************
 * 版权所有 (C)2024, Lishiyun
 *
 * 文件名称： AddTeacherFrame.java
 * 文件标识：无
 * 内容摘要：用于管理员端添加教师信息操作
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

public class AddTeacherFrame extends JFrame {
    /*********************************************************
     * 功能描述：构造函数，用于初始化添加教师信息的图形用户界面框架
     * 输入参数：无
     * 返回值：无
     * 其它说明：设置框架标题、关闭操作、大小和位置，并使其可见。创建包含教师信息输入组件和按钮的面板，设置组件字体、布局及按钮背景透明等样式，为按钮添加事件监听器，用于处理添加教师信息和重置输入框操作。
     ************************************************************/
    public AddTeacherFrame() {
        super("添加教师信息");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 550);
        setLocationRelativeTo(null);
        setVisible(true);

        JPanel frame = new JPanel();
        add(frame);

        // 教师信息标签和文本框
        JLabel j9 = new JLabel("教师工号:");
        JLabel j10 = new JLabel("教师姓名:");
        JLabel j11 = new JLabel("教师性别:");
        JLabel j12 = new JLabel("教师年龄:");
        JLabel j13 = new JLabel("教师职称:");
        JLabel j14 = new JLabel("手机号:");

        j9.setFont(new Font("楷体", Font.PLAIN, 20));
        j10.setFont(new Font("楷体", Font.PLAIN, 20));
        j11.setFont(new Font("楷体", Font.PLAIN, 20));
        j12.setFont(new Font("楷体", Font.PLAIN, 20));
        j13.setFont(new Font("楷体", Font.PLAIN, 20));
        j14.setFont(new Font("楷体", Font.PLAIN, 20));

        JTextField c9 = new JTextField(15);
        JTextField c10 = new JTextField(15);
        JTextField c11 = new JTextField(15);
        JTextField c12 = new JTextField(15);
        JTextField c13 = new JTextField(15);
        JTextField c14 = new JTextField(15);

        JButton aa = new JButton("确定");
        JButton bb = new JButton("重置");

        aa.setFont(new Font("楷体", Font.PLAIN, 20));
        bb.setFont(new Font("楷体", Font.PLAIN, 20));

        // 设置按钮背景透明
        aa.setContentAreaFilled(false);
        bb.setContentAreaFilled(false);

        // 教师信息组件布局
        j9.setBounds(20, 30, 120, 20);
        c9.setBounds(80, 30, 120, 25);
        j10.setBounds(20, 70, 120, 20);
        c10.setBounds(80, 70, 100, 25);
        j11.setBounds(20, 110, 120, 30);
        c11.setBounds(80, 110, 100, 25);
        j12.setBounds(20, 150, 120, 30);
        c12.setBounds(80, 150, 100, 25);
        j13.setBounds(20, 190, 120, 30);
        c13.setBounds(80, 190, 100, 25);
        j14.setBounds(20, 230, 120, 30);
        c14.setBounds(80, 230, 100, 25);

        aa.setBounds(100, 400, 100, 30);
        bb.setBounds(300, 400, 100, 30);

        frame.setLayout(null);
        frame.add(j9);
        frame.add(c9);
        frame.add(j10);
        frame.add(c10);
        frame.add(j11);
        frame.add(c11);
        frame.add(j12);
        frame.add(c12);
        frame.add(j13);
        frame.add(c13);
        frame.add(j14);
        frame.add(c14);
        frame.add(aa);
        frame.add(bb);

        /*********************************************************
         * 功能描述：“确定”按钮的点击事件处理方法
         * 输入参数：e - 动作事件对象，包含按钮点击的相关信息
         * 返回值：无
         * 其它说明：当点击“确定”按钮时，获取教师信息文本框中的内容，调用 addTeacher 方法尝试将教师信息插入数据库。若插入成功，显示提示信息；若插入失败（可能因教师工号已存在），显示错误信息。若在数据库操作过程中出现 SQL 异常或类加载异常，将打印堆栈跟踪信息。
         ************************************************************/
        aa.addActionListener(e -> {
            String tno = c9.getText().trim();
            String tname = c10.getText().trim();
            String tsex = c11.getText().trim();
            String tage = c12.getText().trim();
            String ttitle = c13.getText().trim();
            String tphone = c14.getText().trim();

            try {
                if (addTeacher(tno, tname, tsex, tage, ttitle, tphone)) {
                    JOptionPane.showMessageDialog(null, "添加教师信息成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "添加教师信息失败，可能教师工号已存在", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });
        /*********************************************************
         * 功能描述：“重置”按钮的点击事件处理方法
         * 输入参数：e - 动作事件对象，包含按钮点击的相关信息
         * 返回值：无
         * 其它说明：当点击“重置”按钮时，清空所有教师信息文本框中的内容。
         ************************************************************/
        bb.addActionListener(e -> {
            c9.setText("");
            c10.setText("");
            c11.setText("");
            c12.setText("");
            c13.setText("");
            c14.setText("");
        });
    }
    /*********************************************************
     * 功能描述：将教师信息插入数据库的方法
     * 输入参数：
     * - tno：教师工号
     * - tname：教师姓名
     * - tsex：教师性别
     * - tage：教师年龄
     * - ttitle：教师职称
     * - tphone：手机号
     * 返回值：true - 插入成功；false - 插入失败（如因 SQL 异常或教师工号已存在等原因）
     * 其它说明：首先获取数据库连接，然后构建插入教师信息的 SQL 语句并预编译。将教师信息参数设置到预编译语句中，执行更新操作并根据影响行数判断插入是否成功。若成功则关闭数据库资源并返回 true；若失败（如出现 SQL 错误），显示错误信息并返回 false。若在获取连接或执行 SQL 操作过程中出现异常，将异常信息打印堆栈跟踪。
     ************************************************************/
    private boolean addTeacher(String tno, String tname, String tsex, String tage, String ttitle, String tphone) throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        String sql = "INSERT INTO dbo.Teacher (Tno, Tname, Tsex, Tage, Ttitle, phone) VALUES (?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, tno);
        pstmt.setString(2, tname);
        pstmt.setString(3, tsex);
        pstmt.setString(4, tage);
        pstmt.setString(5, ttitle);
        pstmt.setString(6, tphone);
        try {
            int result = pstmt.executeUpdate();
            DBUtil.closeResources(conn, pstmt, null);
            return result == 1;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "添加教师信息失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}