begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
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
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JTextField
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
name|AttributeSet
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
name|BadLocationException
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
name|PlainDocument
import|;
end_import

begin_class
specifier|public
class|class
name|LimitedTextField
extends|extends
name|JTextField
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|5615520143950793884L
decl_stmt|;
specifier|public
name|LimitedTextField
parameter_list|(
name|int
name|limit
parameter_list|)
block|{
name|setDocument
argument_list|(
operator|new
name|LimitedDocument
argument_list|(
name|limit
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
class|class
name|LimitedDocument
extends|extends
name|PlainDocument
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|2371422073526259311L
decl_stmt|;
specifier|private
name|int
name|limit
decl_stmt|;
name|LimitedDocument
parameter_list|(
name|int
name|limit
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|limit
operator|=
name|limit
expr_stmt|;
block|}
specifier|public
name|void
name|insertString
parameter_list|(
name|int
name|offset
parameter_list|,
name|String
name|str
parameter_list|,
name|AttributeSet
name|attr
parameter_list|)
throws|throws
name|BadLocationException
block|{
if|if
condition|(
name|str
operator|==
literal|null
condition|)
return|return;
if|if
condition|(
operator|(
name|getLength
argument_list|()
operator|+
name|str
operator|.
name|length
argument_list|()
operator|)
operator|<=
name|limit
condition|)
block|{
name|super
operator|.
name|insertString
argument_list|(
name|offset
argument_list|,
name|str
argument_list|,
name|attr
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

