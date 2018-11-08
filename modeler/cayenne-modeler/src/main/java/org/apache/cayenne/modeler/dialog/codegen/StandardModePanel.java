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
comment|//
end_comment

begin_comment
comment|//import java.awt.BorderLayout;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//public class StandardModePanel extends GeneratorControllerPanel {
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    private DefaultFormBuilder builder;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public StandardModePanel() {
end_comment

begin_comment
comment|//        FormLayout layout = new FormLayout(
end_comment

begin_comment
comment|//                "right:77dlu, 1dlu, fill:100:grow, 1dlu, left:80dlu, 1dlu", "");
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        builder = new DefaultFormBuilder(layout);
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
comment|//        setLayout(new BorderLayout());
end_comment

begin_comment
comment|//        add(builder.getPanel(), BorderLayout.CENTER);
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//}
end_comment

end_unit

