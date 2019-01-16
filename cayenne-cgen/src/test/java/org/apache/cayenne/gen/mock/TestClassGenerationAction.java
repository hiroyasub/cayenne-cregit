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
name|gen
operator|.
name|mock
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|gen
operator|.
name|ClassGenerationAction
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
name|gen
operator|.
name|TemplateType
import|;
end_import

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|TestClassGenerationAction
extends|extends
name|ClassGenerationAction
block|{
specifier|private
name|Collection
argument_list|<
name|StringWriter
argument_list|>
name|writers
decl_stmt|;
specifier|public
name|TestClassGenerationAction
parameter_list|(
name|ClassGenerationAction
name|classGenerationAction
parameter_list|,
name|Collection
argument_list|<
name|StringWriter
argument_list|>
name|writers
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|setCgenConfiguration
argument_list|(
name|classGenerationAction
operator|.
name|getCgenConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|setUtilsFactory
argument_list|(
name|classGenerationAction
operator|.
name|getUtilsFactory
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|writers
operator|=
name|writers
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Writer
name|openWriter
parameter_list|(
name|TemplateType
name|templateType
parameter_list|)
throws|throws
name|Exception
block|{
name|StringWriter
name|writer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|writers
operator|.
name|add
argument_list|(
name|writer
argument_list|)
expr_stmt|;
return|return
name|writer
return|;
block|}
block|}
end_class

end_unit

