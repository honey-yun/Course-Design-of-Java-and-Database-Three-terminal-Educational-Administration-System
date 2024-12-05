/***********************************************************
 * 版权所有 (C)2024, Lishiyun
 *
 * 文件名称： DeleteFrame.java
 * 文件标识：无
 * 内容摘要：管理员添加UI，用于跳转删除信息界面
 * 其它说明：
 * 当前版本： V1.0
 * 作   者：李世赟
 * 完成日期： 20241130
 **********************************************************/
package UI;

import javax.swing.*;
import java.awt.*;

public class DeleteFrame extends JFrame {
    /*********************************************************
     * 功能描述：构造函数，用于初始化删除信息的主框架
     * 输入参数：无
     * 返回值：无
     * 其它说明：设置框架标题、关闭操作、大小和位置，并使其可见。创建主面板，在面板上添加用于删除学生、课程和教师信息的按钮，并为这些按钮设置透明背景和相应的点击事件监听器。当点击按钮时，分别调出对应的删除信息界面。
     ************************************************************/
    public DeleteFrame() {
        super("删除信息");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 650);
        setLocationRelativeTo(null);
        setVisible(true);

        JPanel frame = new JPanel();
        add(frame);

        // 删除学生信息按钮
        JButton deleteStudentButton = new JButton("删除学生信息");
        deleteStudentButton.setFont(new Font("楷体", Font.PLAIN, 20));
        deleteStudentButton.setContentAreaFilled(false);
        deleteStudentButton.setBounds(100, 100, 200, 30);

        // 删除课程信息按钮
        JButton deleteCourseButton = new JButton("删除课程信息");
        deleteCourseButton.setFont(new Font("楷体", Font.PLAIN, 20));
        deleteCourseButton.setContentAreaFilled(false);
        deleteCourseButton.setBounds(100, 200, 200, 30);

        // 删除教师信息按钮
        JButton deleteTeacherButton = new JButton("删除教师信息");
        deleteTeacherButton.setFont(new Font("楷体", Font.PLAIN, 20));
        deleteTeacherButton.setContentAreaFilled(false);
        deleteTeacherButton.setBounds(100, 300, 200, 30);

        frame.setLayout(null);
        frame.add(deleteStudentButton);
        frame.add(deleteCourseButton);
        frame.add(deleteTeacherButton);

        /*********************************************************
         * 功能描述：“删除学生信息”按钮的点击事件处理方法
         * 输入参数：e - 动作事件对象，包含按钮点击的相关信息
         * 返回值：无
         * 其它说明：当点击“删除学生信息”按钮时，创建并显示删除学生信息的界面。
         ************************************************************/
        deleteStudentButton.addActionListener(e -> {
            // 调出删除学生信息界面
            new DeleteStudentFrame().setVisible(true);
        });

        /*********************************************************
         * 功能描述：“删除课程信息”按钮的点击事件处理方法
         * 输入参数：e - 动作事件对象，包含按钮点击的相关信息
         * 返回值：无
         * 其它说明：当点击“删除课程信息”按钮时，创建并显示删除课程信息的界面。
         ************************************************************/
        deleteCourseButton.addActionListener(e -> {
            // 调出删除课程信息界面
            new DeleteCourseFrame().setVisible(true);
        });

        /*********************************************************
         * 功能描述：“删除教师信息”按钮的点击事件处理方法
         * 输入参数：e - 动作事件对象，包含按钮点击的相关信息
         * 返回值：无
         * 其它说明：当点击“删除教师信息”按钮时，创建并显示删除教师信息的界面。
         ************************************************************/
        deleteTeacherButton.addActionListener(e -> {
            // 调出删除教师信息界面
            new DeleteTeacherFrame().setVisible(true);
        });
    }
}