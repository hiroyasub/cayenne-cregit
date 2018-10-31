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
comment|//import com.jgoodies.forms.builder.DefaultFormBuilder;
end_comment

begin_comment
comment|//import com.jgoodies.forms.layout.FormLayout;
end_comment

begin_comment
comment|//import org.apache.cayenne.swing.control.ActionLink;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//import javax.swing.JCheckBox;
end_comment

begin_comment
comment|//import javax.swing.JComboBox;
end_comment

begin_comment
comment|//import javax.swing.JPanel;
end_comment

begin_comment
comment|//import javax.swing.JTextField;
end_comment

begin_comment
comment|//import java.awt.BorderLayout;
end_comment

begin_comment
comment|//import java.awt.FlowLayout;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//public class CustomModePanel extends GeneratorControllerPanel {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    private JComboBox<String> subclassTemplate;
end_comment

begin_comment
comment|//    private JComboBox<String> superclassTemplate;
end_comment

begin_comment
comment|//    protected JCheckBox pairs;
end_comment

begin_comment
comment|//    private JCheckBox overwrite;
end_comment

begin_comment
comment|//    private JCheckBox usePackagePath;
end_comment

begin_comment
comment|//    private JTextField outputPattern;
end_comment

begin_comment
comment|//    private JCheckBox createPropertyNames;
end_comment

begin_comment
comment|//    private JCheckBox createPKProperties;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    private ActionLink manageTemplatesLink;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    CustomModePanel() {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        this.superclassTemplate = new JComboBox<>();
end_comment

begin_comment
comment|//        this.subclassTemplate = new JComboBox<>();
end_comment

begin_comment
comment|//        this.pairs = new JCheckBox();
end_comment

begin_comment
comment|//        this.overwrite = new JCheckBox();
end_comment

begin_comment
comment|//        this.usePackagePath = new JCheckBox();
end_comment

begin_comment
comment|//        this.outputPattern = new JTextField();
end_comment

begin_comment
comment|//        this.createPropertyNames = new JCheckBox();
end_comment

begin_comment
comment|//        this.createPKProperties = new JCheckBox();
end_comment

begin_comment
comment|//        this.manageTemplatesLink = new ActionLink("Customize Templates...");
end_comment

begin_comment
comment|//        manageTemplatesLink.setFont(manageTemplatesLink.getFont().deriveFont(10f));
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        // assemble
end_comment

begin_comment
comment|//        FormLayout layout = new FormLayout(
end_comment

begin_comment
comment|//                "right:77dlu, 1dlu, fill:100:grow, 1dlu, left:80dlu, 1dlu", "");
end_comment

begin_comment
comment|//        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
end_comment

begin_comment
comment|//        builder.setDefaultDialogBorder();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        builder.append("Output Directory:", outputFolder, selectOutputFolder);
end_comment

begin_comment
comment|//        builder.nextLine();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        builder.append("Subclass Template:", subclassTemplate);
end_comment

begin_comment
comment|//        builder.nextLine();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        builder.append("Superclass Template:", superclassTemplate);
end_comment

begin_comment
comment|//        builder.nextLine();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        builder.append("Output Pattern:", outputPattern);
end_comment

begin_comment
comment|//        builder.nextLine();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        builder.append("Make Pairs:", pairs);
end_comment

begin_comment
comment|//        builder.nextLine();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        builder.append("Use Package Path:", usePackagePath);
end_comment

begin_comment
comment|//        builder.nextLine();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        builder.append("Overwrite Subclasses:", overwrite);
end_comment

begin_comment
comment|//        builder.nextLine();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        builder.append("Create Property Names:", createPropertyNames);
end_comment

begin_comment
comment|//        builder.nextLine();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        builder.append("Create PK Properties:", createPKProperties);
end_comment

begin_comment
comment|//        builder.nextLine();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        setLayout(new BorderLayout());
end_comment

begin_comment
comment|//        add(builder.getPanel(), BorderLayout.CENTER);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        JPanel links = new JPanel(new FlowLayout(FlowLayout.RIGHT));
end_comment

begin_comment
comment|//        links.add(manageTemplatesLink);
end_comment

begin_comment
comment|//        add(links, BorderLayout.SOUTH);
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        add(builder.getPanel(), BorderLayout.CENTER);
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public ActionLink getManageTemplatesLink() {
end_comment

begin_comment
comment|//        return manageTemplatesLink;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public JComboBox<String> getSubclassTemplate() {
end_comment

begin_comment
comment|//        return subclassTemplate;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public JComboBox<String> getSuperclassTemplate() {
end_comment

begin_comment
comment|//        return superclassTemplate;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public JCheckBox getOverwrite() {
end_comment

begin_comment
comment|//        return overwrite;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public JCheckBox getPairs() {
end_comment

begin_comment
comment|//        return pairs;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public JCheckBox getUsePackagePath() {
end_comment

begin_comment
comment|//        return usePackagePath;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public JTextField getOutputPattern() {
end_comment

begin_comment
comment|//        return outputPattern;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public JCheckBox getCreatePropertyNames() {
end_comment

begin_comment
comment|//        return createPropertyNames;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public JCheckBox getCreatePKProperties() {
end_comment

begin_comment
comment|//        return createPKProperties;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//}
end_comment

end_unit

