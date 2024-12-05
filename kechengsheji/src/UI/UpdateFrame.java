/***********************************************************
 * 版权所有 (C)2024, Lishiyun
 *
 * 文件名称： UpdateFrame.java
 * 文件标识：无
 * 内容摘要：用于管理员端修改信息功能界面UI，用于跳转各个修改功能界面
 * 其它说明：
 * 当前版本： V1.0
 * 作   者：李世赟
 * 完成日期： 20241130
 **********************************************************/
package UI;

import javax.swing.*;
import java.awt.*;

public class UpdateFrame extends JFrame {
    /*********************************************************
     * 功能描述：构造函数，用于初始化更新信息的主框架
     * 输入参数：无
     * 返回值：无
     * 其它说明：设置框架标题、关闭操作、大小和位置，并使其可见。创建主面板，在面板上添加用于更新学生、课程和教师信息的按钮，并为这些按钮设置透明背景和相应的点击事件监听器。当点击按钮时，分别调出对应的更新信息界面。
     ************************************************************/
    public UpdateFrame() {
        super("修改信息");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 650);
        setLocationRelativeTo(null);
        setVisible(true);

        JPanel frame = new JPanel();
        add(frame);

        // 修改学生信息按钮
        JButton updateStudentButton = new JButton("修改学生信息");
        updateStudentButton.setFont(new Font("楷体", Font.PLAIN, 20));
        updateStudentButton.setContentAreaFilled(false);
        updateStudentButton.setBounds(100, 100, 200, 30);

        // 修改课程信息按钮
        JButton updateCourseButton = new JButton("修改课程信息");
        updateCourseButton.setFont(new Font("楷体", Font.PLAIN, 20));
        updateCourseButton.setContentAreaFilled(false);
        updateCourseButton.setBounds(100, 200, 200, 30);

        // 修改教师信息按钮
        JButton updateTeacherButton = new JButton("修改教师信息");
        updateTeacherButton.setFont(new Font("楷体", Font.PLAIN, 20));
        updateTeacherButton.setContentAreaFilled(false);
        updateTeacherButton.setBounds(100, 300, 200, 30);

        frame.setLayout(null);
        frame.add(updateStudentButton);
        frame.add(updateCourseButton);
        frame.add(updateTeacherButton);

        /*********************************************************
         * 功能描述：“修改学生信息”按钮的点击事件处理方法
         * 输入参数：e - 动作事件对象，包含按钮点击的相关信息
         * 返回值：无
         * 其它说明：当点击“修改学生信息”按钮时，创建并显示修改学生信息的界面。
         ************************************************************/
        updateStudentButton.addActionListener(e -> {
            // 调出修改学生信息界面
            new UpdateStudentFrame().setVisible(true);
        });
/*********************************************************
 * 功能描述：“修改课程信息”按钮的点击事件处理方法
 * 输入参数：e - 动作事件对象，包含按钮点击的相关信息
 * 返回值：无
 * 其它说明：当点击“更修改课程信息”按钮时，创建并显示修改课程信息的界面。
 ************************************************************/
        updateCourseButton.addActionListener(e -> {
            // 调出修改课程信息界面
            new UpdateCourseFrame().setVisible(true);
        });
/*********************************************************
 * 功能描述：“修改教师信息”按钮的点击事件处理方法
 * 输入参数：e - 动作事件对象，包含按钮点击的相关信息
 * 返回值：无
 * 其它说明：当点击“修改教师信息”按钮时，创建并显示修改教师信息的界面。
 ************************************************************/
        updateTeacherButton.addActionListener(e -> {
            // 调出修改教师信息界面
            new UpdateTeacherFrame().setVisible(true);
        });
    }
}