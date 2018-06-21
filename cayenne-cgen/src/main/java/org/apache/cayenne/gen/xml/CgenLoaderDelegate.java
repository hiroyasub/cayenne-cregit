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
name|xml
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|configuration
operator|.
name|xml
operator|.
name|DataChannelMetaData
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
name|configuration
operator|.
name|xml
operator|.
name|NamespaceAwareNestedTagHandler
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
name|project
operator|.
name|extension
operator|.
name|LoaderDelegate
import|;
end_import

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|CgenLoaderDelegate
implements|implements
name|LoaderDelegate
block|{
specifier|private
name|DataChannelMetaData
name|metaData
decl_stmt|;
name|CgenLoaderDelegate
parameter_list|(
name|DataChannelMetaData
name|metaData
parameter_list|)
block|{
name|this
operator|.
name|metaData
operator|=
name|metaData
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getTargetNamespace
parameter_list|()
block|{
return|return
name|CgenExtension
operator|.
name|NAMESPACE
return|;
block|}
annotation|@
name|Override
specifier|public
name|NamespaceAwareNestedTagHandler
name|createHandler
parameter_list|(
name|NamespaceAwareNestedTagHandler
name|parent
parameter_list|,
name|String
name|tag
parameter_list|)
block|{
if|if
condition|(
name|CgenConfigHandler
operator|.
name|CONFIG_TAG
operator|.
name|equals
argument_list|(
name|tag
argument_list|)
condition|)
block|{
return|return
operator|new
name|CgenConfigHandler
argument_list|(
name|parent
argument_list|,
name|metaData
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

