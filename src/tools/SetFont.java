/**
  * @(#)tools.SetFont.java  2008.07.01  
  * Copy Right Information	: Tarena
  * Project					: JavaQQ
  * JDK version used		: jdk1.6.4
  * Comments				: ���������ࡣ
  * Version					: 1.0
  * Sr	Date		Modified By		Why & What is modified
  * 1.	2007.07.17 	С��     		�½�
  **/
package tools;

// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SetFont.java

import java.awt.Color;
import java.awt.Font;
import javax.swing.UIManager;

/**
 * ���������ࡣ
 */
public final class SetFont
{
	/**
	 * ʹ��UIManager����ĳЩ�����ȱʡ���塣
	 * @param font ���塣
	 */
    public static void setFont(Font font)
    {
        UIManager.put("Label.font", font);
        UIManager.put("Label.foreground", Color.black);
        UIManager.put("Button.font", font);
        UIManager.put("Menu.font", font);
        UIManager.put("MenuItem.font", font);
        UIManager.put("CheckBoxMenuItem.font", font);
        UIManager.put("RadioMenuItem.font", font);
        UIManager.put("List.font", font);
        UIManager.put("CheckBox.font", font);
        UIManager.put("RadioButton.font", font);
        UIManager.put("ComboBox.font", font);
        UIManager.put("TextArea.font", font);
        UIManager.put("Table.font", font);
        UIManager.put("TableHeader.font", font);
        UIManager.put("TabbedPane.font", font);
        UIManager.put("Pane.font", font);
        UIManager.put("Frame.font", font);
        UIManager.put("EditorPane.font", font);
        UIManager.put("OptionPane.font", font);
        UIManager.put("TextPane.font", font);
        UIManager.put("TextField.font", font);
        UIManager.put("PasswordField.font", font);
        UIManager.put("Dialog.font", font);
        UIManager.put("Toolbar.font", font);
        UIManager.put("ToolTip.font", font);
        UIManager.put("Slider.font", font);
        UIManager.put("Progress.font", font);
        UIManager.put("FileChooser.font", font);
        UIManager.put("ColorChooser.font", font);
        UIManager.put("TitledBorder.font", font);
    }
}
