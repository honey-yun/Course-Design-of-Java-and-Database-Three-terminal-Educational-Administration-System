/***********************************************************
 * 版权所有 (C)2024, Lishiyun
 *
 * 文件名称： UpdateCourseFrame.java
 * 文件标识：无
 * 内容摘要：用于管理员端修改更新课程信息操作
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

public class UpdateCourseFrame extends JFrame {
    /*********************************************************
     * 功能描述：构造函数，用于初始化修改课程信息界面框架
     * 输入参数：无
     * 返回值：无
     * 其它说明：设置框架标题、关闭操作、大小和位置，并使其可见。创建包含课程号、课程名、学分、工号标签及文本框与修改、重置按钮的面板，设置组件字体、布局及按钮背景透明等样式，为按钮添加事件监听器，用于处理课程信息修改和文本框重置操作，构建修改课程交互界面布局。
     ************************************************************/
    public UpdateCourseFrame() {
        super("修改课程信息");
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

        JButton aa = new JButton("修改");
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
 * 功能描述：“修改”按钮点击事件处理方法
 * 输入参数：e - 动作事件对象，包含按钮点击的相关信息
 * 返回值：无
 * 其它说明：获取课程号文本框输入，调用 queryCourse 查课程是否存在。若存在，获取其他文本框信息，调用 updateCourse 方法修改课程信息并提示结果；若不存在则提示错误。遇 SQL 或类加载异常则打印堆栈跟踪排查问题，确保修改功能稳定可靠。
 ************************************************************/
        aa.addActionListener(e -> {
            String cno = c5.getText().trim();
            String cname = c6.getText().trim();
            String ccredit = c7.getText().trim();
            String tno = c8.getText().trim();

            try {
                if (updateCourse(cno, cname, ccredit, tno)) {
                    JOptionPane.showMessageDialog(null, "修改课程信息成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "修改课程信息失败，可能课程号不存在", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });

/*********************************************************
 * 功能描述：“重置”按钮点击事件处理方法
 * 输入参数：e - 动作事件对象，包含按钮点击触发的事件详情
 * 返回值：无
 * 其它说明：点击时清空所有课程信息文本框内容，方便用户重新输入修改内容，提升交互便利性与数据准确性，优化修改操作流程。
 ************************************************************/
        bb.addActionListener(e -> {
            c5.setText("");
            c6.setText("");
            c7.setText("");
            c8.setText("");
        });
    }

    /*********************************************************
     * 功能描述：查询课程信息的方法
     * 输入参数：cno - 要查询课程的课程号
     * 返回值：true - 课程存在；false - 课程不存在
     * 其它说明：获取数据库连接，构建查询语句，设置课程号参数查询。若结果集有下一条记录则课程存在，关闭资源返回 true；否则返回 false，为修改操作前数据验证提供依据，避免误修改。
     ************************************************************/
    private boolean updateCourse(String cno, String cname, String ccredit, String tno) throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        String sql = "UPDATE dbo.Course SET Cname =?, Ccredit =?, Tno =? WHERE Cno =?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, cname);
        pstmt.setString(2, ccredit);
        pstmt.setString(3, tno);
        pstmt.setString(4, cno);
        int result = pstmt.executeUpdate();
        DBUtil.closeResources(conn, pstmt, null);
        return result == 1;
    }
}