begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|///*****************************************************************
end_comment

begin_comment
comment|// *   Licensed to the Apache Software Foundation (ASF) under one
end_comment

begin_comment
comment|// *  or more contributor license agreements.  See the NOTICE file
end_comment

begin_comment
comment|// *  distributed with this work for additional information
end_comment

begin_comment
comment|// *  regarding copyright ownership.  The ASF licenses this file
end_comment

begin_comment
comment|// *  to you under the Apache License, Version 2.0 (the
end_comment

begin_comment
comment|// *  "License"); you may not use this file except in compliance
end_comment

begin_comment
comment|// *  with the License.  You may obtain a copy of the License at
end_comment

begin_comment
comment|// *
end_comment

begin_comment
comment|// *    http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|// *
end_comment

begin_comment
comment|// *  Unless required by applicable law or agreed to in writing,
end_comment

begin_comment
comment|// *  software distributed under the License is distributed on an
end_comment

begin_comment
comment|// *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
end_comment

begin_comment
comment|// *  KIND, either express or implied.  See the License for the
end_comment

begin_comment
comment|// *  specific language governing permissions and limitations
end_comment

begin_comment
comment|// *  under the License.
end_comment

begin_comment
comment|// ****************************************************************/
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//package org.apache.cayenne.modeler.dialog.codegen;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//import org.apache.cayenne.modeler.Application;
end_comment

begin_comment
comment|//import org.apache.cayenne.swing.components.TopBorder;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//import javax.swing.Box;
end_comment

begin_comment
comment|//import javax.swing.JButton;
end_comment

begin_comment
comment|//import javax.swing.JDialog;
end_comment

begin_comment
comment|//import javax.swing.JLabel;
end_comment

begin_comment
comment|//import javax.swing.JPanel;
end_comment

begin_comment
comment|//import javax.swing.JScrollPane;
end_comment

begin_comment
comment|//import javax.swing.JSplitPane;
end_comment

begin_comment
comment|//import javax.swing.ScrollPaneConstants;
end_comment

begin_comment
comment|//import java.awt.BorderLayout;
end_comment

begin_comment
comment|//import java.awt.Component;
end_comment

begin_comment
comment|//import java.awt.Container;
end_comment

begin_comment
comment|//import java.awt.Dimension;
end_comment

begin_comment
comment|//import java.awt.FlowLayout;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|///**
end_comment

begin_comment
comment|// */
end_comment

begin_comment
comment|//public class CodeGeneratorDialog extends JDialog {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    private JButton generateButton;
end_comment

begin_comment
comment|//    protected JButton cancelButton;
end_comment

begin_comment
comment|//    private JLabel classesCount;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    CodeGeneratorDialog(Component generatorPanel, Component entitySelectorPanel) {
end_comment

begin_comment
comment|//        super(Application.getFrame());
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
end_comment

begin_comment
comment|//        splitPane.setFocusable(false);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        this.generateButton = new JButton("Generate");
end_comment

begin_comment
comment|//        getRootPane().setDefaultButton(generateButton);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        this.cancelButton = new JButton("Cancel");
end_comment

begin_comment
comment|//        this.classesCount = new JLabel("No classes selected");
end_comment

begin_comment
comment|//        classesCount.setFont(classesCount.getFont().deriveFont(10f));
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        JScrollPane scrollPane = new JScrollPane(
end_comment

begin_comment
comment|//                generatorPanel,
end_comment

begin_comment
comment|//                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
end_comment

begin_comment
comment|//                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
end_comment

begin_comment
comment|//        scrollPane.setPreferredSize(new Dimension(630, 500));
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        splitPane.setLeftComponent(entitySelectorPanel);
end_comment

begin_comment
comment|//        splitPane.setRightComponent(scrollPane);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        JPanel messages = new JPanel(new BorderLayout());
end_comment

begin_comment
comment|//        messages.add(classesCount, BorderLayout.WEST);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
end_comment

begin_comment
comment|//        buttons.setBorder(TopBorder.create());
end_comment

begin_comment
comment|//        buttons.add(classesCount);
end_comment

begin_comment
comment|//        buttons.add(Box.createHorizontalStrut(50));
end_comment

begin_comment
comment|//        buttons.add(cancelButton);
end_comment

begin_comment
comment|//        buttons.add(generateButton);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        Container contentPane = getContentPane();
end_comment

begin_comment
comment|//        contentPane.setLayout(new BorderLayout());
end_comment

begin_comment
comment|//        contentPane.add(splitPane, BorderLayout.CENTER);
end_comment

begin_comment
comment|//        contentPane.add(buttons, BorderLayout.SOUTH);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        setTitle("Code Generation");
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public JButton getCancelButton() {
end_comment

begin_comment
comment|//        return cancelButton;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public JButton getGenerateButton() {
end_comment

begin_comment
comment|//        return generateButton;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public JLabel getClassesCount() {
end_comment

begin_comment
comment|//        return classesCount;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//}
end_comment

end_unit

