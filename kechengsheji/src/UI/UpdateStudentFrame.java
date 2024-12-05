/***********************************************************
 * 版权所有 (C)2024, Lishiyun
 *
 * 文件名称： UpdateStudentFrame.java
 * 文件标识：无
 * 内容摘要：用于管理员端修改学生信息操作
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

public class UpdateStudentFrame extends JFrame {
    /*********************************************************
     * 功能描述：构造函数，用于初始化修改学生信息的图形用户界面框架
     * 输入参数：无
     * 返回值：无
     * 其它说明：设置框架标题、关闭操作、大小和位置，并使其可见。创建包含学号、姓名、性别、年龄、专业标签及文本框与修改、重置按钮的面板，设置组件字体、布局及按钮背景透明等样式，为按钮添加事件监听器，用于处理学生信息修改和文本框重置操作，构建修改学生信息交互界面布局。
     ************************************************************/
    public UpdateStudentFrame() {
        super("修改学生信息");
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

        JButton aa = new JButton("修改");
        JButton bb = new JButton("重置");

        aa.setFont(new Font("楷体", Font.PLAIN, 20));
        bb.setFont(new Font("楷体", Font.PLAIN, 20));

        // 设置按钮背景透明
        aa.setContentAreaFilled(false);
        bb.setContentAreaFilled(false);

        // 学生信息组件布局
        j.setBounds(20, 30, 120, 20);
        c.setBounds(80, 30, 120, 25);
        j1.setBounds(20, 70, 120, 20);
        c1.setBounds(80, 70, 100, 25);
        j2.setBounds(20, 110, 120, 30);
        c2.setBounds(80, 110, 100, 25);
        j3.setBounds(20, 150, 120, 30);
        c3.setBounds(80, 150, 100, 25);
        j4.setBounds(20, 190, 120, 30);
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
 * 功能描述：“修改”按钮点击事件处理方法
 * 输入参数：e - 动作事件对象，包含按钮点击的相关信息
 * 返回值：无
 * 其它说明：获取学号文本框输入，调用 queryStudent 查学生是否存在。若存在，获取其他文本框信息，调用 updateStudent 方法修改学生信息并提示结果；若不存在则提示错误。遇 SQL 或类加载异常则打印堆栈跟踪排查问题，确保修改功能稳定可靠。
 ************************************************************/
        aa.addActionListener(e -> {
            String sno = c.getText().trim();
            String sname = c1.getText().trim();
            String ssex = c2.getText().trim();
            String sage = c3.getText().trim();
            String sdept = c4.getText().trim();

            try {
                if (updateStudent(sno, sname, ssex, sage, sdept)) {
                    JOptionPane.showMessageDialog(null, "修改学生信息成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "修改学生信息失败，可能学号不存在", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });
/*********************************************************
 * 功能描述：“重置”按钮点击事件处理方法
 * 输入参数：e - 动作事件对象，包含按钮点击触发的事件详情
 * 返回值：无
 * 其它说明：点击时清空所有学生信息文本框内容，方便用户重新输入修改内容，提升交互便利性与数据准确性，优化修改操作流程。
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
     * 功能描述：修改学生信息的方法
     * 输入参数：
     * - sno - 学号
     * - sname - 姓名
     * - ssex - 性别
     * - sage - 年龄
     * - sdept - 专业
     * 返回值：true - 更新成功；false - 更新失败（如学号不存在或 SQL 操作异常）
     * 其它说明：获取数据库连接，构建更新学生信息的 SQL 语句并预编译，设置学生各属性参数后执行更新操作。根据影响行数判断更新是否成功，成功则关闭资源返回 true；失败则显示错误信息并返回 false，确保数据库学生数据一致性与准确性。
     ************************************************************/
    private boolean updateStudent(String sno, String sname, String ssex, String sage, String sdept) throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        String sql = "UPDATE dbo.Student SET Sname =?, Ssex =?, Sage =?, Sdept =? WHERE Sno =?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, sname);
        pstmt.setString(2, ssex);
        pstmt.setString(3, sage);
        pstmt.setString(4, sdept);
        pstmt.setString(5, sno);
        int result = pstmt.executeUpdate();
        DBUtil.closeResources(conn, pstmt, null);
        return result == 1;
    }
}