/***********************************************************
 * 版权所有 (C)2024, Lishiyun
 *
 * 文件名称： DeleteStudentFrame.java
 * 文件标识：无
 * 内容摘要：用于管理员端删除学生信息操作
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

public class DeleteStudentFrame extends JFrame {
    /*********************************************************
     * 功能描述：构造函数，用于初始化删除学生信息的图形用户界面框架
     * 输入参数：无
     * 返回值：无
     * 其它说明：设置框架标题、关闭操作、大小和位置，并使其可见。创建包含学生学号标签与文本框及删除、重置按钮的面板，设置组件字体、布局及按钮背景透明等样式，为按钮添加事件监听器，用于处理学生信息删除和文本框重置操作，构建删除学生信息交互界面布局。
     ************************************************************/
    public DeleteStudentFrame() {
        super("删除学生信息");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 550);
        setLocationRelativeTo(null);
        setVisible(true);

        JPanel frame = new JPanel();
        add(frame);

        // 学生信息标签和文本框
        JLabel j = new JLabel("学号:");
        j.setFont(new Font("楷体", Font.PLAIN, 20));
        JTextField c = new JTextField(15);

        JButton aa = new JButton("删除");
        JButton bb = new JButton("重置");

        aa.setFont(new Font("楷体", Font.PLAIN, 20));
        bb.setFont(new Font("楷体", Font.PLAIN, 20));

        // 设置按钮背景透明
        aa.setContentAreaFilled(false);
        bb.setContentAreaFilled(false);

        // 组件布局
        j.setBounds(20, 30, 120, 20);
        c.setBounds(80, 30, 120, 25);
        aa.setBounds(100, 400, 100, 30);
        bb.setBounds(300, 400, 100, 30);

        frame.setLayout(null);
        frame.add(j);
        frame.add(c);
        frame.add(aa);
        frame.add(bb);
        /*********************************************************
         * 功能描述：“删除”按钮点击事件处理方法
         * 输入参数：e - 动作事件对象，包含按钮点击的相关信息
         * 返回值：无
         * 其它说明：获取学号文本框输入内容，调用 deleteStudent 方法尝试从数据库删除学生信息。若删除成功，显示提示信息；若失败（如学号不存在），显示错误信息。遇 SQL 或类加载异常则打印堆栈跟踪排查问题。
         ************************************************************/
        aa.addActionListener(e -> {
            String sno = c.getText().trim();

            try {
                if (deleteStudent(sno)) {
                    JOptionPane.showMessageDialog(null, "删除学生信息成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "删除学生信息失败，可能学号不存在", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });
        /*********************************************************
         * 功能描述：“重置”按钮点击事件处理方法
         * 输入参数：e - 动作事件对象，包含按钮点击触发的事件详情
         * 返回值：无
         * 其它说明：点击时清空学号文本框内容，方便用户重新输入学号进行删除操作，提升交互便利性与数据准确性。
         ************************************************************/
        bb.addActionListener(e -> {
            c.setText("");
        });
    }

    /*********************************************************
     * 功能描述：从数据库删除学生信息的方法
     * 输入参数：sno - 要删除学生的学号
     * 返回值：true - 删除成功；false - 删除失败（如学号不存在或 SQL 操作异常）
     * 其它说明：获取数据库连接，构建删除学生信息的 SQL 语句并预编译，设置学号参数后执行更新操作。根据影响行数判断删除是否成功，成功则关闭资源返回 true；失败则显示错误信息并返回 false，确保数据库数据一致性与完整性。
     ************************************************************/
    private boolean deleteStudent(String sno) throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        String sql = "DELETE FROM dbo.Student WHERE Sno =?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, sno);
        try {
            int result = pstmt.executeUpdate();
            DBUtil.closeResources(conn, pstmt, null);
            return result == 1;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "删除学生信息失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}