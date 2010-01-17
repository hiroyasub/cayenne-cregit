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
operator|.
name|style
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Color
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Font
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|SyntaxStyle
block|{
specifier|private
name|Color
name|color
decl_stmt|;
specifier|private
name|Font
name|font
decl_stmt|;
specifier|public
name|SyntaxStyle
parameter_list|(
name|Color
name|color
parameter_list|,
name|Font
name|fontStyle
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|color
operator|=
name|color
expr_stmt|;
name|this
operator|.
name|font
operator|=
name|fontStyle
expr_stmt|;
block|}
specifier|public
name|Font
name|getFont
parameter_list|()
block|{
return|return
name|font
return|;
block|}
specifier|public
name|Color
name|getColor
parameter_list|()
block|{
return|return
name|color
return|;
block|}
block|}
end_class

end_unit

