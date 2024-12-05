/***********************************************************
 * 版权所有 (C)2024, Lishiyun
 *
 * 文件名称： AddFrame.java
 * 文件标识：无
 * 内容摘要：管理员添加UI，用于跳转添加信息界面
 * 其它说明：
 * 当前版本： V1.0
 * 作   者：李世赟
 * 完成日期： 20241130
 **********************************************************/
package UI;

import javax.swing.*;
import java.awt.*;

public class AddFrame extends JFrame {
    /*********************************************************
     * 功能描述：构造函数，用于初始化添加信息的主框架
     * 输入参数：无
     * 返回值：无
     * 其它说明：设置框架标题、关闭操作、大小和位置，并使其可见。创建主面板，在面板上添加用于添加学生、课程和教师信息的按钮，并为这些按钮设置透明背景和相应的点击事件监听器。当点击按钮时，分别调出对应的添加信息界面。
     ************************************************************/
    public AddFrame() {
        super("添加信息");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 650);
        setLocationRelativeTo(null);
        setVisible(true);

        JPanel frame = new JPanel();
        add(frame);

        // 添加学生信息按钮
        JButton addStudentButton = new JButton("添加学生信息");
        addStudentButton.setFont(new Font("楷体", Font.PLAIN, 20));
        addStudentButton.setContentAreaFilled(false);
        addStudentButton.setBounds(100, 100, 200, 30);

        // 添加课程信息按钮
        JButton addCourseButton = new JButton("添加课程信息");
        addCourseButton.setFont(new Font("楷体", Font.PLAIN, 20));
        addCourseButton.setContentAreaFilled(false);
        addCourseButton.setBounds(100, 200, 200, 30);

        // 添加教师信息按钮
        JButton addTeacherButton = new JButton("添加教师信息");
        addTeacherButton.setFont(new Font("楷体", Font.PLAIN, 20));
        addTeacherButton.setContentAreaFilled(false);
        addTeacherButton.setBounds(100, 300, 200, 30);

        frame.setLayout(null);
        frame.add(addStudentButton);
        frame.add(addCourseButton);
        frame.add(addTeacherButton);


        /*********************************************************
         * 功能描述：“添加学生信息”按钮的点击事件处理方法
         * 输入参数：e - 动作事件对象，包含按钮点击的相关信息
         * 返回值：无
         * 其它说明：当点击“添加学生信息”按钮时，创建并显示添加学生信息的界面。
         ************************************************************/
        addStudentButton.addActionListener(e -> {
            // 调出添加学生信息界面
            new AddStudentFrame().setVisible(true);
        });


        /*********************************************************
         * 功能描述：“添加课程信息”按钮的点击事件处理方法
         * 输入参数：e - 动作事件对象，包含按钮点击的相关信息
         * 返回值：无
         * 其它说明：当点击“添加课程信息”按钮时，创建并显示添加课程信息的界面。
         ************************************************************/
        addCourseButton.addActionListener(e -> {
            // 调出添加课程信息界面
            new AddCourseFrame().setVisible(true);
        });

        /*********************************************************
         * 功能描述：“添加教师信息”按钮的点击事件处理方法
         * 输入参数：e - 动作事件对象，包含按钮点击的相关信息
         * 返回值：无
         * 其它说明：当点击“添加教师信息”按钮时，创建并显示添加教师信息的界面。
         ************************************************************/
        addTeacherButton.addActionListener(e -> {
            // 调出添加教师信息界面
            new AddTeacherFrame().setVisible(true);
        });
    }
}