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
comment|//import javax.swing.JButton;
end_comment

begin_comment
comment|//import javax.swing.JPanel;
end_comment

begin_comment
comment|//import javax.swing.JTextField;
end_comment

begin_comment
comment|//import java.util.ArrayList;
end_comment

begin_comment
comment|//import java.util.Collection;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|///**
end_comment

begin_comment
comment|// * A generic panel that is a superclass of generator panels, defining common fields.
end_comment

begin_comment
comment|// *
end_comment

begin_comment
comment|// */
end_comment

begin_comment
comment|//public class GeneratorControllerPanel extends JPanel {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    protected Collection<StandardPanelComponent> dataMapLines;
end_comment

begin_comment
comment|//    protected JTextField outputFolder;
end_comment

begin_comment
comment|//    protected JButton selectOutputFolder;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public GeneratorControllerPanel() {
end_comment

begin_comment
comment|//        this.dataMapLines = new ArrayList<>();
end_comment

begin_comment
comment|//        this.outputFolder = new JTextField();
end_comment

begin_comment
comment|//        this.selectOutputFolder = new JButton("Select");
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public JTextField getOutputFolder() {
end_comment

begin_comment
comment|//        return outputFolder;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public JButton getSelectOutputFolder() {
end_comment

begin_comment
comment|//        return selectOutputFolder;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public Collection<StandardPanelComponent> getDataMapLines() {
end_comment

begin_comment
comment|//        return dataMapLines;
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//}
end_comment

end_unit

