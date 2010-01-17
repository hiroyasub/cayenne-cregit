begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|swing
operator|.
name|components
operator|.
name|textpane
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|text
operator|.
name|StyledEditorKit
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|text
operator|.
name|ViewFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|swing
operator|.
name|components
operator|.
name|textpane
operator|.
name|syntax
operator|.
name|SyntaxConstant
import|;
end_import

begin_class
specifier|public
class|class
name|EditorKit
extends|extends
name|StyledEditorKit
block|{
specifier|private
name|ViewFactory
name|xmlViewFactory
decl_stmt|;
specifier|private
name|String
name|contentType
decl_stmt|;
specifier|public
name|EditorKit
parameter_list|(
name|SyntaxConstant
name|syntaxConstant
parameter_list|)
block|{
name|contentType
operator|=
name|syntaxConstant
operator|.
name|getContentType
argument_list|()
expr_stmt|;
name|xmlViewFactory
operator|=
operator|new
name|TextPaneViewFactory
argument_list|(
name|syntaxConstant
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|ViewFactory
name|getViewFactory
parameter_list|()
block|{
return|return
name|xmlViewFactory
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getContentType
parameter_list|()
block|{
return|return
name|contentType
return|;
block|}
block|}
end_class

end_unit

