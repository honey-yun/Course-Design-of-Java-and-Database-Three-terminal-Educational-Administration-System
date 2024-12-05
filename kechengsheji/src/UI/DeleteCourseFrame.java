/***********************************************************
 * 版权所有 (C)2024, Lishiyun
 *
 * 文件名称： DeleteCourseFrame.java
 * 文件标识：无
 * 内容摘要：用于管理员端删除课程信息操作
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

public class DeleteCourseFrame extends JFrame {
    /*********************************************************
     * 功能描述：构造函数，用于初始化删除课程信息界面框架
     * 输入参数：无
     * 返回值：无
     * 其它说明：设置框架标题、关闭操作、大小和位置，并使其可见。创建包含课程号标签与文本框及删除、重置按钮的面板，设置组件字体、布局及按钮背景透明等样式，为按钮添加事件监听器，用于处理课程删除和文本框重置操作，构建删除课程交互界面布局。
     ************************************************************/
    public DeleteCourseFrame() {
        super("删除课程信息");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 550);
        setLocationRelativeTo(null);
        setVisible(true);

        JPanel frame = new JPanel();
        add(frame);

        // 课程信息标签和文本框（这里只需要课程号用于确定要删除的课程记录）
        JLabel j5 = new JLabel("课程号:");
        j5.setFont(new Font("楷体", Font.PLAIN, 20));
        JTextField c5 = new JTextField(15);

        JButton aa = new JButton("删除");
        JButton bb = new JButton("重置");

        aa.setFont(new Font("楷体", Font.PLAIN, 20));
        bb.setFont(new Font("楷体", Font.PLAIN, 20));

        // 设置按钮背景透明
        aa.setContentAreaFilled(false);
        bb.setContentAreaFilled(false);

        // 组件布局
        j5.setBounds(20, 30, 120, 20);
        c5.setBounds(80, 30, 120, 25);
        aa.setBounds(100, 400, 100, 30);
        bb.setBounds(300, 400, 100, 30);

        frame.setLayout(null);
        frame.add(j5);
        frame.add(c5);
        frame.add(aa);
        frame.add(bb);
/*********************************************************
 * 功能描述：“删除”按钮点击事件处理方法
 * 输入参数：e - 动作事件对象，包含按钮点击的相关信息
 * 返回值：无
 * 其它说明：获取课程号文本框输入内容，调用 deleteCourse 方法尝试从数据库删除课程。若删除成功，显示提示信息；若失败（如课程号不存在），显示错误信息。遇 SQL 或类加载异常则打印堆栈跟踪排查问题。
 ************************************************************/
        aa.addActionListener(e -> {
            String cno = c5.getText().trim();

            try {
                if (deleteCourse(cno)) {
                    JOptionPane.showMessageDialog(null, "删除课程信息成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "删除课程信息失败，可能课程号不存在", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });
/*********************************************************
 * 功能描述：“重置”按钮点击事件处理方法
 * 输入参数：e - 动作事件对象，包含按钮点击触发的事件详情
 * 返回值：无
 * 其它说明：点击时清空课程号文本框内容，方便用户重新输入课程号进行删除操作，提升交互便利性与数据准确性。
 ************************************************************/
        bb.addActionListener(e -> {
            c5.setText("");
        });
    }

    /*********************************************************
     * 功能描述：从数据库删除课程信息的方法
     * 输入参数：cno - 要删除课程的课程号
     * 返回值：true - 删除成功；false - 删除失败（如课程号不存在或 SQL 操作异常）
     * 其它说明：获取数据库连接，构建删除课程的 SQL 语句并预编译，设置课程号参数后执行更新操作。根据影响行数判断删除是否成功，成功则关闭资源返回 true；失败则显示错误信息并返回 false，确保数据库数据一致性与完整性。
     ************************************************************/
    private boolean deleteCourse(String cno) throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        String sql = "DELETE FROM dbo.Course WHERE Cno =?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, cno);
        try {
            int result = pstmt.executeUpdate();
            DBUtil.closeResources(conn, pstmt, null);
            return result == 1;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "删除课程信息失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}